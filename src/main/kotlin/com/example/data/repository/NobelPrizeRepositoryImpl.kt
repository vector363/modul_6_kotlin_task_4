package com.example.data.repository

import com.example.data.datasource.InMemoryDataSource
import com.example.domain.model.Laureate
import com.example.domain.model.NobelPrize
import com.example.domain.model.User
import com.example.domain.repository.NobelPrizeRepository

class NobelPrizeRepositoryImpl : NobelPrizeRepository {

    override fun getAllPrizes(): List<NobelPrize> = InMemoryDataSource.getAllPrizes()

    override fun getPrizeByYearAndCategory(year: String, category: String): NobelPrize? =
        InMemoryDataSource.getPrizeByYearAndCategory(year, category)

    override fun getLaureatesByPrize(year: String, category: String): List<Laureate>? =
        getPrizeByYearAndCategory(year, category)?.laureates

    override fun getUserByUsername(username: String): User? =
        InMemoryDataSource.getUser(username)
}