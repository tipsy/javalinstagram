package javalinstagram.like

import io.javalin.http.Context
import javalinstagram.currentUser

object LikeController {

    fun create(ctx: Context) {
        LikeDao.add(photoId = ctx.queryParam("photo-id")!!, ownerId = ctx.currentUser!!)
        ctx.status(201)
    }

    fun delete(ctx: Context) {
        LikeDao.delete(photoId = ctx.queryParam("photo-id")!!, ownerId = ctx.currentUser!!)
        ctx.status(204)
    }

}
