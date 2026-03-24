package com.example.presentation.routes

import com.example.domain.usecase.LoginUseCase
import com.example.presentation.models.LoginRequest
import com.example.presentation.models.LoginResponse
import com.example.data.repository.NobelPrizeRepositoryImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    val repository = NobelPrizeRepositoryImpl()
    val loginUseCase = LoginUseCase(repository)

    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = loginUseCase.execute(request.username, request.password)

            if (token != null) {
                val user = loginUseCase.getUser(request.username)
                call.respond(
                    HttpStatusCode.OK,
                    LoginResponse(
                        token = token,
                        userId = user?.id ?: 0,
                        username = user?.username ?: "",
                        email = user?.email ?: ""
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "Invalid username or password")
                )
            }
        }
    }
}