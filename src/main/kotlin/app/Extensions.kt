package app

import io.javalin.Context
import javax.servlet.http.Cookie

var Context.currentUser: String?
    get() = this.sessionAttribute("signed-in-user")
    set(username) {
        if (username != null) {
            this.sessionAttribute("signed-in-user", username)
            this.cookie(Cookie("signed-in-user", username).apply {
                path = "/"
            })
        } else {
            this.sessionAttribute("signed-in-user", null)
            this.removeCookie("signed-in-user")
        }

    }
