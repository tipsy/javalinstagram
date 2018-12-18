package javalinstagram.like

import javalinstagram.hikari

object LikeDao {

    fun add(photoId: String, ownerId: String) = hikari.connection.use { connection ->
        connection.prepareStatement("insert into like (photoid, ownerid) values (?, ?)").apply {
            setString(1, photoId)
            setString(2, ownerId)
        }.executeUpdate()
    }

    fun delete(photoId: String, ownerId: String) = hikari.connection.use { connection ->
        connection.prepareStatement("delete from like where photoid=? and ownerid=?").apply {
            setString(1, photoId)
            setString(2, ownerId)
        }.executeUpdate()
    }

}
