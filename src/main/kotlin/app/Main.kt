package app

import app.like.LikeController
import app.photo.PhotoController
import app.user.UserController
import io.javalin.Handler
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.util.Header
import io.javalin.rendering.template.JavalinVelocity
import io.javalin.security.SecurityUtil.roles
import io.javalin.staticfiles.Location
import java.nio.file.Files
import java.nio.file.Paths

enum class Role : io.javalin.security.Role { LOGGED_IN, ANYONE }

fun main(args: Array<String>) {

    Files.createDirectories(Paths.get("user-uploads/static/p"));

    val app = Javalin.create().apply {
        port(7000)
        enableStaticFiles("user-uploads", Location.EXTERNAL)
        sessionHandler { Session.fileSessionHandler() }
        accessManager { handler, ctx, permitted ->
            when {
                permitted.contains(Role.ANYONE) -> handler.handle(ctx)
                ctx.currentUser != null && permitted.contains(Role.LOGGED_IN) -> handler.handle(ctx)
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin")
                else -> ctx.status(401)
            }
        }
    }.start()

    app.routes {
        get("/", render("/view/index.vue"), roles(Role.LOGGED_IN))
        get("/signin",render("/view/signin.vue"), roles(Role.ANYONE))
        get("/my-photos", render("/view/my-photos.vue"), roles(Role.LOGGED_IN))
        path("api") {
            path("photos") {
                get(PhotoController::getForQuery, roles(Role.LOGGED_IN))
                post(PhotoController::upload, roles(Role.LOGGED_IN))
            }
            path("likes") {
                post(LikeController::create, roles(Role.LOGGED_IN))
                delete(LikeController::delete, roles(Role.LOGGED_IN))
            }
            get("signout", UserController::signOut, roles(Role.ANYONE))
            post("signin", UserController::signIn, roles(Role.ANYONE))
            post("signup", UserController::signUp, roles(Role.ANYONE))
        }
    }

}


private fun render(templatePath: String) = Handler { ctx ->
    val template = JavalinVelocity.render(templatePath, mapOf()).split("@body:")
    val fullPage = JavalinVelocity.render("/view/_frame/vueLayout.vm", mapOf(
            "headContent" to template[0].removePrefix("@head:"), // load component into head, avoid flicker
            "bodyContent" to template[1] // trigger components from body
    ))
    ctx.html(fullPage)
}
