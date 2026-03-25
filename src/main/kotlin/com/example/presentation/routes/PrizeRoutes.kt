package com.example.presentation.routes

import com.example.data.repository.PrizeRepositoryImpl
import com.example.domain.usecase.*
import com.example.presentation.models.LaureateResponse
import com.example.presentation.models.PrizeResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.prizeRoutes() {
    val repository = PrizeRepositoryImpl()
    val getAllPrizesUseCase = GetAllPrizesUseCase(repository)
    val getPrizeByYearAndCategoryUseCase = GetPrizeByYearAndCategoryUseCase(repository)
    val getPrizeLaureatesUseCase = GetPrizeLaureatesUseCase(repository)
    val addToFavoritesUseCase = AddToFavoritesUseCase(repository)
    val removeFromFavoritesUseCase = RemoveFromFavoritesUseCase(repository)
    val getUserFavoritesUseCase = GetUserFavoritesUseCase(repository)

    route("/prizes") {
        authenticate {
            // GET /prizes - список всех премий
            get {
                val prizes = getAllPrizesUseCase.execute()
                val response = prizes.map { PrizeResponse.fromDomain(it) }
                call.respond(response)
            }

            // GET /prizes/{year}/{category} - детальная премия
            get("/{year}/{category}") {
                val year = call.parameters["year"] ?: throw IllegalArgumentException("Year is required")
                val category = call.parameters["category"] ?: throw IllegalArgumentException("Category is required")

                val prize = getPrizeByYearAndCategoryUseCase.execute(year, category)
                if (prize != null) {
                    call.respond(PrizeResponse.fromDomain(prize))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "Prize not found for year $year and category $category")
                    )
                }
            }

            // GET /prizes/{year}/{category}/laureates - список лауреатов
            get("/{year}/{category}/laureates") {
                val year = call.parameters["year"] ?: throw IllegalArgumentException("Year is required")
                val category = call.parameters["category"] ?: throw IllegalArgumentException("Category is required")

                val laureates = getPrizeLaureatesUseCase.execute(year, category)
                if (laureates != null) {
                    val response = laureates.map { LaureateResponse.fromDomain(it) }
                    call.respond(response)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "Laureates not found for year $year and category $category")
                    )
                }
            }
        }
    }

    // Защищенные эндпоинты для избранного
    route("/users/me") {
        authenticate {
            // GET /users/me/prizes - избранные премии
            get("/prizes") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt() ?:
                throw IllegalArgumentException("User not found")

                val favorites = getUserFavoritesUseCase.execute(userId)
                val response = favorites.map { PrizeResponse.fromDomain(it) }
                call.respond(response)
            }

            // POST /users/me/prizes/{prizeId} - добавить в избранное
            post("/prizes/{prizeId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt() ?:
                throw IllegalArgumentException("User not found")
                val prizeId = call.parameters["prizeId"]?.toIntOrNull() ?:
                throw IllegalArgumentException("Invalid prizeId")

                addToFavoritesUseCase.execute(userId, prizeId)
                call.respond(HttpStatusCode.Created, mapOf("message" to "Added to favorites"))
            }

            // DELETE /users/me/prizes/{prizeId} - удалить из избранного
            delete("/prizes/{prizeId}") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt() ?:
                throw IllegalArgumentException("User not found")
                val prizeId = call.parameters["prizeId"]?.toIntOrNull() ?:
                throw IllegalArgumentException("Invalid prizeId")

                removeFromFavoritesUseCase.execute(userId, prizeId)
                call.respond(HttpStatusCode.OK, mapOf("message" to "Removed from favorites"))
            }
        }
    }
}