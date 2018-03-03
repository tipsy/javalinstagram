package app

import java.util.*

data class User(val id: String, val password: String, val created: Date)
data class Photo(val id: String, val ownerId: String, val likes: Int, val isLiked: Boolean, val created: Date)
data class Like(val photoId: String, val ownerId: String)

data class Credentials(val username: String = "", val password: String = "")
