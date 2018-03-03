package app

import app.like.LikeController
import app.photo.PhotoController
import app.user.UserController
import io.javalin.ApiBuilder.*
import io.javalin.Handler
import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.embeddedserver.Location
import io.javalin.security.Role
import java.nio.file.Files
import java.nio.file.Paths

enum class UserRole : Role { LOGGED_IN }

fun main(args: Array<String>) {

    Files.createDirectories(Paths.get("user-uploads/static/p"));

    val app = Javalin.create().apply {
        port(7000)
        enableDynamicGzip()
        enableStandardRequestLogging()
        enableStaticFiles("user-uploads", Location.EXTERNAL)
        enableStaticFiles("/public", Location.CLASSPATH)
        maxBodySizeForRequestCache(4096)
        accessManager { handler, ctx, permittedRoles ->
            if (ctx.currentUser != null && permittedRoles.contains(UserRole.LOGGED_IN)) {
                handler.handle(ctx)
            } else if (ctx.header(Header.ACCEPT)?.contains("html") == true) {
                ctx.redirect("/signin")
            } else {
                ctx.status(401)
            }
        }
    }.start()

    // sign in
    app.get("/signin", renderPage("/velocity/signin.vm"))
    app.get("/signout", UserController::signOut)
    app.post("/signin", UserController::signIn)
    app.post("/signup", UserController::signUp)

    app.routes {

        get("/", renderPage("/velocity/index.vm"), listOf(UserRole.LOGGED_IN))
        get("/my-photos", renderPage("/velocity/my-photos.vm"), listOf(UserRole.LOGGED_IN))

        path("api") {
            path("photos") {
                get(PhotoController::getForQuery, listOf(UserRole.LOGGED_IN))
                post(PhotoController::upload, listOf(UserRole.LOGGED_IN))
            }
            path("likes") {
                post(LikeController::create, listOf(UserRole.LOGGED_IN))
                delete(LikeController::delete, listOf(UserRole.LOGGED_IN))
            }
        }
    }

}

private fun renderPage(templatePath: String): Handler {
    return Handler { ctx -> ctx.renderVelocity(templatePath) }
}
