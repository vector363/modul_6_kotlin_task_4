package com.example.domain.usecase

import com.example.domain.repository.PrizeRepository

class RemoveFromFavoritesUseCase(
    private val repository: PrizeRepository
) {
    suspend fun execute(userId: Int, prizeId: Int) {
        repository.removeFromFavorites(userId, prizeId)
    }
}