package javalinstagram

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.core.util.Header
import io.javalin.http.staticfiles.Location
import javalinstagram.Role.ANYONE
import javalinstagram.Role.LOGGED_IN
import javalinstagram.account.AccountController
import javalinstagram.like.LikeController
import javalinstagram.photo.PhotoController
import org.jdbi.v3.core.Jdbi

val Database = Jdbi.create("jdbc:sqlite:javalinstagram.db")

fun main() {

    val app = Javalin.create {
        it.addStaticFiles("user-uploads", Location.EXTERNAL)
        it.addStaticFiles("src/main/resources/public", Location.EXTERNAL)
        it.addSinglePageRoot("/", "src/main/resources/public/index.html", Location.EXTERNAL)
        it.sessionHandler{ Session.fileSessionHandler() }
        it.accessManager{ handler, ctx, permitted ->
            when {
                permitted.contains(ANYONE) -> handler.handle(ctx)
                ctx.currentUser != null && permitted.contains(LOGGED_IN) -> handler.handle(ctx)
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin") // redirect browser to signing
                else -> ctx.status(401)
            }
        }
    }.start(7000)

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
            path("account") {
                post("sign-up", AccountController::signUp, roles(ANYONE))
                post("sign-in", AccountController::signIn, roles(ANYONE))
                post("sign-out", AccountController::signOut, roles(ANYONE))
            }
        }
    }

}
