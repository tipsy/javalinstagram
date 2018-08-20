package app

import app.like.LikeController
import app.photo.PhotoController
import app.user.UserController
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.Javalin
import io.javalin.core.util.Header
import io.javalin.security.Role
import io.javalin.staticfiles.Location
import java.nio.file.Files
import java.nio.file.Paths

enum class UserRole : Role { LOGGED_IN, ANYONE }

fun main(args: Array<String>) {

    Files.createDirectories(Paths.get("user-uploads/static/p"));

    val app = Javalin.create().apply {
        port(7000)
        enableStaticFiles("user-uploads", Location.EXTERNAL)
        accessManager { handler, ctx, permitted ->
            when {
                permitted.contains(UserRole.ANYONE) -> handler.handle(ctx)
                ctx.currentUser != null && permitted.contains(UserRole.LOGGED_IN) -> handler.handle(ctx)
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin")
                else -> ctx.status(401)
            }
        }
    }.start()

    app.routes {
        get("/signin", { ctx -> ctx.render("/view/signin.vm") }, setOf(UserRole.ANYONE))
        get("/", { ctx -> ctx.render("/view/index.vm") }, setOf(UserRole.LOGGED_IN))
        get("/my-photos", { ctx -> ctx.render("/view/my-photos.vm") }, setOf(UserRole.LOGGED_IN))
        path("api") {
            path("photos") {
                get(PhotoController::getForQuery, setOf(UserRole.LOGGED_IN))
                post(PhotoController::upload, setOf(UserRole.LOGGED_IN))
            }
            path("likes") {
                post(LikeController::create, setOf(UserRole.LOGGED_IN))
                delete(LikeController::delete, setOf(UserRole.LOGGED_IN))
            }
            get("signout", UserController::signOut, setOf(UserRole.ANYONE))
            post("signin", UserController::signIn, setOf(UserRole.ANYONE))
            post("signup", UserController::signUp, setOf(UserRole.ANYONE))
        }
    }

}
