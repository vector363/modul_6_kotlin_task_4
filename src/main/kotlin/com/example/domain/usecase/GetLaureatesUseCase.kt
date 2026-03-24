package com.example.domain.usecase

import com.example.domain.model.Laureate
import com.example.domain.repository.NobelPrizeRepository

class GetLaureatesUseCase(
    private val repository: NobelPrizeRepository
) {
    fun execute(year: String, category: String): List<Laureate>? {
        return repository.getLaureatesByPrize(year, category)
    }
}