package com.example.domain.usecase

import com.example.domain.model.Laureate
import com.example.domain.repository.PrizeRepository

class GetPrizeLaureatesUseCase(
    private val repository: PrizeRepository
) {
    suspend fun execute(year: String, category: String): List<Laureate>? {
        return repository.getPrizeByYearAndCategory(year, category)?.laureates
    }
}