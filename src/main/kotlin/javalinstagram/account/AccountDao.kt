package javalinstagram.account

import javalinstagram.Hikari
import javalinstagram.map
import javalinstagram.parseTimestamp
import java.util.*

data class Account(val id: String, val password: String, val created: Date)

object AccountDao {

    fun add(userId: String, password: String) = Hikari.connection.use { connection ->
        connection.prepareStatement("insert into user (id, password) values (?, ?)").apply {
            setString(1, userId)
            setString(2, password)
        }.executeUpdate()
    }

    fun findById(id: String): Account? = Hikari.connection.use { connection ->
        connection.prepareStatement("select * from user where id=?").apply {
            setString(1, id)
        }.executeQuery().map { rs ->
            Account(rs.getString("id"), rs.getString("password"), rs.parseTimestamp("created"))
        }.firstOrNull()
    }

}
