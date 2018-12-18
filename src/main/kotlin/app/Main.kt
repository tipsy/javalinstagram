package app

import app.like.LikeController
import app.photo.PhotoController
import app.user.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.util.Header
import io.javalin.security.SecurityUtil.roles
import io.javalin.staticfiles.Location
import java.nio.file.Files
import java.nio.file.Paths
import app.Role.LOGGED_IN
import app.Role.ANYONE

enum class Role : io.javalin.security.Role { LOGGED_IN, ANYONE }

fun main(args: Array<String>) {

    Files.createDirectories(Paths.get("user-uploads/static/p"));

    val app = Javalin.create().apply {
        port(7000)
        enableStaticFiles("user-uploads", Location.EXTERNAL)
        enableStaticFiles("/public", Location.CLASSPATH)
        enableSinglePageMode("/", "/public/index.html")
        sessionHandler { Session.fileSessionHandler() }
        accessManager { handler, ctx, permitted ->
            when {
                permitted.contains(ANYONE) -> handler.handle(ctx)
                ctx.currentUser != null && permitted.contains(LOGGED_IN) -> handler.handle(ctx)
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin") // redirect browser to signing
                else -> ctx.status(401)
            }
        }
    }.start()

    app.routes {
        path("api") {
            path("photos") {
                get(PhotoController::getForQuery, roles(LOGGED_IN))
                post(PhotoController::upload, roles(LOGGED_IN))
            }
            path("likes") {
                post(LikeController::create, roles(LOGGED_IN))
                delete(LikeController::delete, roles(LOGGED_IN))
            }
            get("signout", UserController::signOut, roles(ANYONE))
            post("signin", UserController::signIn, roles(ANYONE))
            post("signup", UserController::signUp, roles(ANYONE))
        }
    }

}
