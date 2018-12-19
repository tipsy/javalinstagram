package javalinstagram.util

import javalinstagram.Database

// database-setup/reset
fun main(args: Array<String>) {
    println("Setting up database...")
    Database.useHandle<Exception> { handle ->
        handle.execute("drop table if exists user")
        handle.execute("create table user (id string, password string, created timestamp DEFAULT CURRENT_TIMESTAMP)")
        handle.execute("drop table if exists photo")
        handle.execute("create table photo (id string, ownerid string, created timestamp DEFAULT CURRENT_TIMESTAMP)")
        handle.execute("drop table if exists like")
        handle.execute("create table like (photoid string, ownerid string)")
    }
    println("Database setup complete!")
}
