package com.example.domain.repository

import com.example.domain.model.Prize

interface PrizeRepository {
    suspend fun getAllPrizes(): List<Prize>
    suspend fun getPrizeById(prizeId: Int): Prize?
    suspend fun getPrizeByYearAndCategory(year: String, category: String): Prize?
    suspend fun savePrize(prize: Prize)
    suspend fun savePrizes(prizes: List<Prize>)
    suspend fun addToFavorites(userId: Int, prizeId: Int)
    suspend fun removeFromFavorites(userId: Int, prizeId: Int)
    suspend fun getUserFavorites(userId: Int): List<Prize>
    suspend fun isFavorite(userId: Int, prizeId: Int): Boolean
}