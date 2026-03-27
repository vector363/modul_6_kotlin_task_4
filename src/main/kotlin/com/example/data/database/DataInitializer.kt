package com.example.data.database

import com.example.data.repository.PrizeRepositoryImpl
import com.example.data.service.NobelPrizeApiService


object DataInitializer {
    suspend fun initializeData() {
        val prizeRepository = PrizeRepositoryImpl()

        val existingPrizes = prizeRepository.getAllPrizes()
        if (existingPrizes.isNotEmpty()) {
            println("Data already exists in database, skipping import")
            return
        }

        println("Fetching data from Nobel Prize API")

        try {
            val apiPrizes = NobelPrizeApiService.fetchPrizes(limit = 50)
            val domainPrizes = apiPrizes.map { NobelPrizeApiService.mapToDomain(it) }

            prizeRepository.savePrizes(domainPrizes)

            println("Imported ${domainPrizes.size} prizes to database")
        } catch (e: Exception) {
            println("Failed to import data: ${e.message}")
        }
    }
}