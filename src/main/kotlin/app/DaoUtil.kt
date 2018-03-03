package app

import io.javalin.core.JavalinServlet
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.text.SimpleDateFormat

private val log = LoggerFactory.getLogger(JavalinServlet::class.java)

fun <T> queryAndMap(query: String, fn: (ResultSet) -> T): Iterable<T> = performAction { connection ->
    return@performAction connection.createStatement().executeQuery(query).map { fn(it) }.toList()
}

fun <T> performAction(func: (Connection) -> T): T {
    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection("jdbc:sqlite:javalinstagram.db")
        return func.invoke(connection)
    } catch (e: SQLException) {
        log.warn("Failed to invoke function with connection", e)
    } finally {
        try {
            connection?.close()
        } catch (e: SQLException) {
            log.warn("Failed to close connection", e)
        }
    }
    throw SQLException()
}

// really should get rid of this
fun ResultSet.parseTimestamp(timestamp: String) = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getString(timestamp))

fun <T> ResultSet.map(fn: (ResultSet) -> T): Iterable<T> {
    val resultSet = this
    val iterator = object : Iterator<T> {
        override fun hasNext(): Boolean = resultSet.next()
        override fun next(): T = fn(resultSet)
    }
    return object : Iterable<T> {
        override fun iterator(): Iterator<T> = iterator
    }
}

// database-setup
fun main(args: Array<String>) {

    performAction { connection ->
        connection.createStatement().apply {
            executeUpdate("drop table if exists user")
            executeUpdate("create table user (id string, password string, created timestamp DEFAULT CURRENT_TIMESTAMP)")
            executeUpdate("drop table if exists photo")
            executeUpdate("create table photo (id string, ownerid string, created timestamp DEFAULT CURRENT_TIMESTAMP)")
            executeUpdate("drop table if exists like")
            executeUpdate("create table like (photoid string, ownerid string)")
        }
    }

}
