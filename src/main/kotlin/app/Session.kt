package app

import org.eclipse.jetty.server.session.*
import java.io.File

object Session {

    fun fileSessionHandler() = SessionHandler().apply {
        httpOnly = true
        sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                val baseDir = File(System.getProperty("java.io.tmpdir"))
                this.storeDir = File(baseDir, "javalin-session-store").apply { mkdir() }
            }
        }
    }

}
