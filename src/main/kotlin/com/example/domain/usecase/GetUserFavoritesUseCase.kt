package com.example.domain.usecase

import com.example.domain.model.Prize
import com.example.domain.repository.PrizeRepository

class GetUserFavoritesUseCase(
    private val repository: PrizeRepository
) {
    suspend fun execute(userId: Int): List<Prize> {
        return repository.getUserFavorites(userId)
    }
}