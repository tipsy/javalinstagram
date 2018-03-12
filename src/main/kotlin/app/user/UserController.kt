package app.user

import app.Credentials
import app.currentUser
import io.javalin.Context
import io.javalin.HaltException
import io.javalin.core.util.Header
import org.mindrot.jbcrypt.BCrypt

object UserController {

    fun signIn(ctx: Context) {
        val (username, password) = getCredentialsOrDieTrying(ctx)
        val user = UserDao.findById(username)
        if (user != null && BCrypt.checkpw(password, user.password)) {
            ctx.status(200)
            ctx.currentUser = username
        } else {
            ctx.status(400).json("Incorrect username/password")
        }
    }

    fun signUp(ctx: Context) {
        val (username, password) = getCredentialsOrDieTrying(ctx)
        val user = UserDao.findById(username)
        if (user == null) {
            UserDao.add(userId = username, password = BCrypt.hashpw(password, BCrypt.gensalt()))
            ctx.status(201)
            ctx.currentUser = username
        } else {
            ctx.status(400).json("app.User '$username' already exists")
        }
    }

    private fun getCredentialsOrDieTrying(ctx: Context): Credentials {
        val credentials = ctx.bodyAsClass(Credentials::class.java)
        if (credentials.username.isBlank() || credentials.password.isBlank()) {
            throw HaltException(400, "Missing username/password")
        }
        return credentials
    }

    fun signOut(ctx: Context) {
        ctx.currentUser = null
        if (ctx.header(Header.ACCEPT)?.contains("html") == true) {
            ctx.redirect("/signin")
        }
    }

}
