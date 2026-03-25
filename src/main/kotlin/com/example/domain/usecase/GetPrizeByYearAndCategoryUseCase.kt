package com.example.domain.usecase

import com.example.domain.model.Prize
import com.example.domain.repository.PrizeRepository

class GetPrizeByYearAndCategoryUseCase(
    private val repository: PrizeRepository
) {
    suspend fun execute(year: String, category: String): Prize? {
        return repository.getPrizeByYearAndCategory(year, category)
    }
}