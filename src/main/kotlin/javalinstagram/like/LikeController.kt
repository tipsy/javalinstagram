package javalinstagram.like

import javalinstagram.currentUser
import io.javalin.Context

object LikeController {

    fun create(ctx: Context) {
        LikeDao.add(photoId = ctx.validatedQueryParam("photo-id").getOrThrow(), ownerId = ctx.currentUser!!)
        ctx.status(201)
    }

    fun delete(ctx: Context) {
        LikeDao.delete(photoId = ctx.validatedQueryParam("photo-id").getOrThrow(), ownerId = ctx.currentUser!!)
        ctx.status(204)
    }

}
