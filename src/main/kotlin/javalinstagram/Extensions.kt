package javalinstagram

import io.javalin.Context
import java.sql.ResultSet
import java.text.SimpleDateFormat

var Context.currentUser: String?
    get() = this.sessionAttribute("current-user")
    set(username) = this.sessionAttribute("current-user", username)

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
