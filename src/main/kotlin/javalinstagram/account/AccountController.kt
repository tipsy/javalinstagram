package javalinstagram.account

import io.javalin.Context
import io.javalin.core.util.Header
import javalinstagram.currentUser
import org.mindrot.jbcrypt.BCrypt

data class Credentials(val username: String = "", val password: String = "")

object AccountController {

    fun signIn(ctx: Context) {
        val (username, password) = ctx.validatedBody<Credentials>()
                .check({ it.username.isNotBlank() && it.password.isNotBlank() })
                .getOrThrow()
        val user = AccountDao.findById(username)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            ctx.status(200)
            ctx.currentUser = username
        } else {
            ctx.status(400).json("Incorrect username/password")
        }
    }

    fun signUp(ctx: Context) {
        val (username, password) = ctx.validatedBody<Credentials>()
                .check({ it.username.isNotBlank() && it.password.isNotBlank() })
                .getOrThrow()
        val user = AccountDao.findById(username)
        if (user == null) {
            AccountDao.add(userId = username, password = BCrypt.hashpw(password, BCrypt.gensalt()))
            ctx.status(201)
            ctx.currentUser = username
        } else {
            ctx.status(400).json("app.Account '$username' already exists")
        }
    }

    fun signOut(ctx: Context) {
        ctx.currentUser = null
        if (ctx.header(Header.ACCEPT)?.contains("html") == true) {
            ctx.redirect("/signin")
        }
    }

}
