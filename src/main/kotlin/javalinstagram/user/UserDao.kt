package javalinstagram.user

import javalinstagram.User
import javalinstagram.hikari
import javalinstagram.map
import javalinstagram.parseTimestamp

object UserDao {

    fun add(userId: String, password: String) = hikari.connection.use { connection ->
        connection.prepareStatement("insert into user (id, password) values (?, ?)").apply {
            setString(1, userId)
            setString(2, password)
        }.executeUpdate()
    }

    fun findById(id: String): User? = hikari.connection.use { connection ->
        connection.prepareStatement("select * from user where id=?").apply {
            setString(1, id)
        }.executeQuery().map { rs ->
            User(rs.getString("id"), rs.getString("password"), rs.parseTimestamp("created"))
        }.firstOrNull()
    }

}
