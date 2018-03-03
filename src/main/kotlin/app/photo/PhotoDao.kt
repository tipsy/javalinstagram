package app

object PhotoDao {

    fun add(photoId: String, ownerId: String) = performAction { connection ->
        connection.prepareStatement("insert into photo (id, ownerid) values(?, ?)").apply {
            setString(1, photoId)
            setString(2, ownerId)
        }.executeUpdate()
    }

    // this should be done as a SQL-query (WHERE photo.ownerid = ?) if performance is important
    fun findByOwnerId(ownerId: String): List<Photo> = all(ownerId).filter { it.ownerId == ownerId }

    fun all(likeOwner: String): List<Photo> = performAction { connection ->
        return@performAction connection.prepareStatement("""
            |SELECT
            |    photo.*,
            |    COUNT(like.photoid) as like_count,
            |    (SELECT 1 WHERE like.ownerid = ?) as is_liked
            |FROM photo LEFT JOIN like ON photo.id = like.photoid
            |GROUP BY photo.id
            |ORDER BY created desc""".trimMargin()).apply {
            setString(1, likeOwner)
        }.executeQuery().map { rs ->
            Photo(rs.getString("id"), rs.getString("ownerid"), rs.getInt("like_count"), rs.getBoolean("is_liked"), rs.parseTimestamp("created"))
        }.toList()
    }

}
