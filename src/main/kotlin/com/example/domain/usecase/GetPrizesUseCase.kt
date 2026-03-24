package com.example.domain.usecase

import com.example.domain.model.NobelPrize
import com.example.domain.repository.NobelPrizeRepository

class GetPrizesUseCase(
    private val repository: NobelPrizeRepository
) {
    fun execute(): List<NobelPrize> {
        return repository.getAllPrizes()
    }
}