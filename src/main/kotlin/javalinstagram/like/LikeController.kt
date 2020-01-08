package javalinstagram.like

import io.javalin.http.Context
import javalinstagram.currentUser

object LikeController {

    fun create(ctx: Context) {
        LikeDao.add(photoId = ctx.validPhotoId(), ownerId = ctx.currentUser!!)
        ctx.status(201)
    }

    fun delete(ctx: Context) {
        LikeDao.delete(photoId = ctx.validPhotoId(), ownerId = ctx.currentUser!!)
        ctx.status(204)
    }

    private fun Context.validPhotoId() = this.queryParam<String>("photo-id").check({ it.length > 10 }).get()

}
