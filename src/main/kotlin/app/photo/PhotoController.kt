package app.photo

import app.currentUser
import io.javalin.Context
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import java.io.File
import java.util.*

object PhotoController {

    fun upload(ctx: Context) {
        ctx.uploadedFile("photo")?.let { photoFile ->
            val photo = File.createTempFile("temp", "upload").apply {
                photoFile.content.copyTo(this.outputStream())
            }
            val id = UUID.randomUUID().toString().replace("-", "")
            Thumbnails.of(photo)
                    .crop(Positions.CENTER)
                    .size(800, 800)
                    .outputFormat("jpg")
                    .toFile(File("user-uploads/static/p/$id.jpg"))
            PhotoDao.add(photoId = "$id.jpg", ownerId = ctx.currentUser!!)
            ctx.status(201)
        } ?: ctx.status(400).json("No photo found")
    }

    fun getForQuery(ctx: Context) {
        val command = ctx.queryParam("command") ?: "all"
        val ownerId = ctx.queryParam("owner-id")
        when {
            command == "all" && ownerId?.isNotEmpty() == true -> {
                ctx.json(PhotoDao.findByOwnerId(ownerId))
            }
            command == "all" -> ctx.json(PhotoDao.all(ctx.currentUser!!))
            command == "latest" -> ctx.json(PhotoDao.all(ctx.currentUser!!).take(8))
        }
    }

}
