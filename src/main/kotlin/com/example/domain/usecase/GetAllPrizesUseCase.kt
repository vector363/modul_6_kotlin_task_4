package com.example.domain.usecase

import com.example.domain.model.Prize
import com.example.domain.repository.PrizeRepository

class GetAllPrizesUseCase(
    private val repository: PrizeRepository
) {
    suspend fun execute(): List<Prize> {
        return repository.getAllPrizes()
    }
}