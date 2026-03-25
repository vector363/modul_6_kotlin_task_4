package com.example.domain.usecase

import com.example.domain.repository.PrizeRepository

class AddToFavoritesUseCase(
    private val repository: PrizeRepository
) {
    suspend fun execute(userId: Int, prizeId: Int) {
        repository.addToFavorites(userId, prizeId)
    }
}