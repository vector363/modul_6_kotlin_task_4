package com.example.presentation.models

import com.example.domain.model.Prize
import kotlinx.serialization.Serializable

@Serializable
data class PrizeResponse(
    val id: Int,
    val year: String,
    val category: String,
    val prizeAmount: Int?,
    val dateAwarded: String?,
    val laureates: List<LaureateResponse>
) {
    companion object {
        fun fromDomain(prize: Prize): PrizeResponse {
            return PrizeResponse(
                id = prize.id,
                year = prize.awardYear,
                category = prize.category,
                prizeAmount = prize.prizeAmount,
                dateAwarded = prize.dateAwarded,
                laureates = prize.laureates.map { LaureateResponse.fromDomain(it) }
            )
        }
    }
}