package app

import io.javalin.Context

var Context.currentUser: String?
    get() = this.sessionAttribute("signed-in-user")
    set(username) {
        if (username != null) {
            this.sessionAttribute("signed-in-user", username)
            this.cookie("signed-in-user", username)
        } else {
            this.sessionAttribute("signed-in-user", null)
            this.removeCookie("signed-in-user")
        }

    }
