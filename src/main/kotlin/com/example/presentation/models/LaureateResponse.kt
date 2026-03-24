package com.example.presentation.models

import com.example.domain.model.Laureate
import kotlinx.serialization.Serializable

@Serializable
data class LaureateResponse(
    val id: String,
    val fullName: String,
    val motivation: String,
    val birthDate: String?,
    val birthPlace: String?
) {
    companion object {
        fun fromDomain(laureate: Laureate): LaureateResponse {
            return LaureateResponse(
                id = laureate.id,
                fullName = laureate.fullName,
                motivation = laureate.motivation,
                birthDate = laureate.birthDate,
                birthPlace = laureate.birthPlace
            )
        }
    }
}