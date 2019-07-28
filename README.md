# Javalinstagram

A (very limited) Instagram clone built with

* [Javalin](https://javalin.io/) - Kotlin web server framework
* [JDBI](http://jdbi.org/) - Database connection pool and object mapping
* [SQLite](https://www.sqlite.org/index.html) - Database
* [Vue](https://vuejs.org/) - JavaScript view library
* [Vue router](https://router.vuejs.org/) - Client side routing
* [axios](https://github.com/axios/axios) - Client side http library
* Native JavaScript modules (no Webpack or other frontend build tools)

## Functionality

* Create account/sign-in/sign-out (uses bcrypt to hash passwords)
* Upload photo (resize/crop)
* Like/unlike photo
* Get photos (for one user and for all users)

## Application structure

### Backend

The application is packaged by feature rather than layer. This means that (for example)
all functionality related to photos (like a `Photo` class, a `PhotoDao`, a `PhotoController`)
are in a `photo` package, instead of having a `controller` package with controllers for different features:

```text
src
└───main
    └───kotlin
        └───javalinstagram
            ├───account
            ├───like
            ├───photo
            ├───util
            └───Main.kt // backend entry point
```

Security is handled by Javalin's `AccessManager`, and set per endpoint. Sessions are handled by Jetty.

### Frontend

The frontend is split into `components` and `views`. Both components and views are "single-file", meaning HTML/JS/CSS are all contained in one file.
Components are re-usable and can be included in one or more views:

```text
src
└───main
    └───resources
        └───vue
            ├───components
            ├───views
            ├───layout.html
```

There is no Webpack or other build system, the application uses [JavalinVue](https://javalin.io/documentation#vue-support)
for rapid development.

## Screenshot
