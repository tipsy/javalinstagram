package app.user

import app.User
import app.map
import app.parseTimestamp
import app.performAction

object UserDao {

    fun add(userId: String, password: String) = performAction { connection ->
        connection.prepareStatement("insert into user (id, password) values (?, ?)").apply {
            setString(1, userId)
            setString(2, password)
        }.executeUpdate()
    }

    fun findById(id: String): User? = performAction { connection ->
        return@performAction connection.prepareStatement("select * from user where id=?").apply {
            setString(1, id)
        }.executeQuery().map { rs ->
            User(rs.getString("id"), rs.getString("password"), rs.parseTimestamp("created"))
        }.firstOrNull()
    }

}
