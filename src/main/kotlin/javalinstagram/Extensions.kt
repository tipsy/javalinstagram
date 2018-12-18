package javalinstagram

import io.javalin.Context

var Context.currentUser: String?
    get() = this.sessionAttribute("current-user")
    set(username) = this.sessionAttribute("current-user", username)
