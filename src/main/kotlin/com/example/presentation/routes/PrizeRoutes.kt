package com.example.presentation.routes

import com.example.domain.usecase.GetLaureatesUseCase
import com.example.domain.usecase.GetPrizeDetailUseCase
import com.example.domain.usecase.GetPrizesUseCase
import com.example.presentation.models.LaureateResponse
import com.example.presentation.models.PrizeResponse
import com.example.data.repository.NobelPrizeRepositoryImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.prizeRoutes() {
    val repository = NobelPrizeRepositoryImpl()
    val getPrizesUseCase = GetPrizesUseCase(repository)
    val getPrizeDetailUseCase = GetPrizeDetailUseCase(repository)
    val getLaureatesUseCase = GetLaureatesUseCase(repository)

    route("/prizes") {
        authenticate {
            // GET /prizes - список всех премий
            get {
                val prizes = getPrizesUseCase.execute()
                val response = prizes.map { PrizeResponse.fromDomain(it) }
                call.respond(response)
            }

            // GET /prizes/{year}/{category} - детальная премия
            get("/{year}/{category}") {
                val year = call.parameters["year"] ?: throw IllegalArgumentException("Year is required")
                val category = call.parameters["category"] ?: throw IllegalArgumentException("Category is required")

                val prize = getPrizeDetailUseCase.execute(year, category)
                if (prize != null) {
                    call.respond(PrizeResponse.fromDomain(prize))
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        mapOf("error" to "Prize not found for year $year and category $category")
                    )
                }
            }

            // GET /prizes/{year}/{category}/laureates - список лауреатов премии
            get("/{year}/{category}/laureates") {
                val year = call.parameters["year"] ?: throw IllegalArgumentException("Year is required")
                val category = call.parameters["category"] ?: throw IllegalArgumentException("Category is required")

                val laureates = getLaureatesUseCase.execute(year, category)
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
}