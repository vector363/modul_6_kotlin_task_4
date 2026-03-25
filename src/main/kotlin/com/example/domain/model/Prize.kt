package com.example.domain.model

data class Prize(
    val id: Int,
    val awardYear: String,
    val category: String,
    val prizeAmount: Int?,
    val dateAwarded: String?,
    val laureates: List<Laureate> = emptyList()
)

data class Laureate(
    val id: String,
    val fullName: String,
    val motivation: String,
    val portion: String,
    val birthDate: String?,
    val birthPlace: String?
)