package app.photo

import app.currentUser
import io.javalin.Context
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.*

object PhotoController {

    fun upload(ctx: Context) {
        ctx.uploadedFile("app/photohoto")?.let { photoFile ->
            val photo = File.createTempFile("temp", "upload").apply {
                FileUtils.copyInputStreamToFile(photoFile.content, this)
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
        when {
            command == "all" && ctx.queryParam("owner-id")?.isNotEmpty() == true -> {
                ctx.json(PhotoDao.findByOwnerId(ctx.queryParam("owner-id")!!))
            }
            command == "all" -> ctx.json(PhotoDao.all(ctx.currentUser!!))
            command == "latest" -> ctx.json(PhotoDao.all(ctx.currentUser!!).take(8))
        }
    }

}
