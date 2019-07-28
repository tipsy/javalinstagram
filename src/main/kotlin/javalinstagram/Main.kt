package javalinstagram

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.core.compression.Brotli
import io.javalin.core.compression.Gzip
import io.javalin.core.security.SecurityUtil.roles
import io.javalin.core.util.Header
import io.javalin.http.staticfiles.Location
import io.javalin.plugin.rendering.vue.JavalinVue
import io.javalin.plugin.rendering.vue.VueComponent
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
        it.enableWebjars()
        it.sessionHandler { Session.fileSessionHandler() }
        it.accessManager { handler, ctx, permitted ->
            when {
                permitted.contains(ANYONE) -> handler.handle(ctx)
                ctx.currentUser != null && permitted.contains(LOGGED_IN) -> handler.handle(ctx)
                ctx.header(Header.ACCEPT)?.contains("html") == true -> ctx.redirect("/signin") // redirect browser to signing
                else -> ctx.status(401)
            }
        }
        JavalinVue.stateFunction = { ctx -> mapOf("currentUser" to ctx.currentUser) }
        it.compressionStrategy(Brotli(4), Gzip(6))
    }.start(7000)

    app.routes {
        get("/signin", VueComponent("<signin-view></signin-view>"), roles(ANYONE))
        get("/", VueComponent("<feed-view></feed-view>"), roles(LOGGED_IN))
        get("/my-photos", VueComponent("<my-photos-view></my-photos-view>"), roles(LOGGED_IN))
    }

    app.error(404, "html", VueComponent("<not-found-view></not-found-view>"))

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
