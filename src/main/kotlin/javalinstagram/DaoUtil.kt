package javalinstagram

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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


