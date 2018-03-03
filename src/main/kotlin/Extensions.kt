import io.javalin.Context

var Context.currentUser: String?
    get() = this.request().session.getAttribute("signed-in-user") as String?
    set(username) {
        if (username != null) {
            this.request().session.setAttribute("signed-in-user", username)
            this.cookie("signed-in-user", username)
        } else {
            this.request().session.removeAttribute("signed-in-user")
            this.removeCookie("signed-in-user")
        }

    }
