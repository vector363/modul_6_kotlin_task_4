package com.example.presentation.models

import com.example.domain.model.NobelPrize
import kotlinx.serialization.Serializable

@Serializable
data class PrizeResponse(
    val year: String,
    val category: String,
    val prizeAmount: Int?,
    val laureates: List<LaureateResponse>
) {
    companion object {
        fun fromDomain(prize: NobelPrize): PrizeResponse {
            return PrizeResponse(
                year = prize.year,
                category = prize.category,
                prizeAmount = prize.prizeAmount,
                laureates = prize.laureates.map { LaureateResponse.fromDomain(it) }
            )
        }
    }
}