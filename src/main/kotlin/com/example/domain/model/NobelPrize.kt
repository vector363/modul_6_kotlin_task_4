package com.example.domain.model

data class NobelPrize(
    val year: String,
    val category: String,
    val prizeAmount: Int?,
    val laureates: List<Laureate>
)

data class Laureate(
    val id: String,
    val fullName: String,
    val motivation: String,
    val birthDate: String?,
    val birthPlace: String?
)