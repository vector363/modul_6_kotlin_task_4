package com.example.plugins

import com.example.presentation.routes.authRoutes
import com.example.presentation.routes.prizeRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRoutes() //post
        prizeRoutes() //get
    }
}