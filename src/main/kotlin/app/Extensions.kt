package app

import io.javalin.Context

var Context.currentUser: String?
    get() = this.sessionAttribute("signed-in-user")
    set(username) = this.sessionAttribute("signed-in-user", username)
