package com.example.domain.repository

import com.example.domain.model.Laureate
import com.example.domain.model.NobelPrize
import com.example.domain.model.User

interface NobelPrizeRepository {

    fun getAllPrizes(): List<NobelPrize>

    fun getPrizeByYearAndCategory(year: String, category: String): NobelPrize?

    fun getLaureatesByPrize(year: String, category: String): List<Laureate>?

    fun getUserByUsername(username: String): User?
}