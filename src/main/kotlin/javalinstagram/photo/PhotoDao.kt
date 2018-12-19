package javalinstagram.photo

import javalinstagram.Database
import javalinstagram.parseTimestamp
import java.util.*

data class Photo(val id: String, val ownerId: String, val likes: Int, val isLiked: Boolean, val created: Date)

object PhotoDao {

    fun add(photoId: String, ownerId: String) = Database.useHandle<Exception> { handle ->
        handle.createUpdate("insert into photo (id, ownerid) values(:id, :ownerid)")
                .bind("id", photoId)
                .bind("ownerid", ownerId)
                .execute()
    }

    // this should be done as a SQL-query (WHERE photo.ownerid = ?) if performance is important
    fun findByOwnerId(ownerId: String): List<Photo> = all(ownerId).filter { it.ownerId == ownerId }

    fun all(userId: String) = Database.withHandle<List<Photo>, Exception> { handle ->
        handle.createQuery("""
            |SELECT
            |    photo.*,
            |    COUNT(like.photoid) as like_count,
            |    (SELECT 1 WHERE like.ownerid = :ownerid) as is_liked
            |FROM photo LEFT JOIN like ON photo.id = like.photoid
            |GROUP BY photo.id
            |ORDER BY created desc""".trimMargin()
        ).bind("ownerid", userId).map { rs, ctx ->
            Photo(rs.getString("id"), rs.getString("ownerid"), rs.getInt("like_count"), rs.getBoolean("is_liked"), rs.parseTimestamp("created"))
        }.list()
    }

}
