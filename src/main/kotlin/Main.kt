import io.javalin.Javalin

fun main(args: Array<String>) {
    val app = Javalin.start(0)
    app.get("/") { ctx -> ctx.result("Hello World") }
}
