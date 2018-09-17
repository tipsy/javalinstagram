package app

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.text.SimpleDateFormat

val hikari = HikariDataSource(HikariConfig().apply {
    setJdbcUrl("jdbc:sqlite:javalinstagram.db");
    addDataSourceProperty("cachePrepStmts", "true");
    addDataSourceProperty("prepStmtCacheSize", "250");
    addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
})

// really should get rid of this
fun ResultSet.parseTimestamp(timestamp: String) = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this.getString(timestamp))

fun HikariDataSource.preparedStatement(string: String) = this.connection.use { it.prepareStatement(string) }

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

// database-setup/reset
fun main(args: Array<String>) {

    hikari.connection.use { connection ->
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
