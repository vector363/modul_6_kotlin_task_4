package com.example.data.repository

import com.example.data.database.tables.*
import com.example.domain.model.Laureate
import com.example.domain.model.Prize
import com.example.domain.repository.PrizeRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PrizeRepositoryImpl : PrizeRepository {

    override suspend fun getAllPrizes(): List<Prize> = newSuspendedTransaction {
        PrizesTable
            .selectAll()
            .map { rowToPrize(it) }
    }

    override suspend fun getPrizeById(prizeId: Int): Prize? = newSuspendedTransaction {
        PrizesTable
            .selectAll()
            .where { PrizesTable.id eq prizeId }
            .map { rowToPrize(it) }
            .singleOrNull()
    }

    override suspend fun getPrizeByYearAndCategory(year: String, category: String): Prize? = newSuspendedTransaction {
        PrizesTable
            .selectAll()
            .where { (PrizesTable.awardYear eq year) and (PrizesTable.category eq category) }
            .map { rowToPrize(it) }
            .singleOrNull()
    }

    override suspend fun savePrize(prize: Prize) = newSuspendedTransaction {
        // Проверяем, существует ли уже такая премия
        val existingPrize = PrizesTable
            .selectAll()
            .where { (PrizesTable.awardYear eq prize.awardYear) and (PrizesTable.category eq prize.category) }
            .singleOrNull()

        val prizeId = if (existingPrize != null) {
            existingPrize[PrizesTable.id]
        } else {
            // Вставляем новую премию и получаем ID
            val insertStatement = PrizesTable.insert {
                it[PrizesTable.awardYear] = prize.awardYear
                it[PrizesTable.category] = prize.category
                it[PrizesTable.prizeAmount] = prize.prizeAmount
                it[PrizesTable.dateAwarded] = prize.dateAwarded
            }
            insertStatement[PrizesTable.id] ?: throw Exception("Failed to get generated ID")
        }

        // Сохраняем лауреатов с привязкой к prizeId
        for (laureate: Laureate in prize.laureates) {
            val existingLaureate = LaureatesTable
                .selectAll()
                .where { (LaureatesTable.id eq laureate.id) and (LaureatesTable.prizeId eq prizeId) }
                .singleOrNull()

            if (existingLaureate == null) {
                LaureatesTable.insert {
                    it[LaureatesTable.id] = laureate.id
                    it[LaureatesTable.prizeId] = prizeId
                    it[LaureatesTable.fullName] = laureate.fullName
                    it[LaureatesTable.motivation] = laureate.motivation
                    it[LaureatesTable.portion] = laureate.portion
                    it[LaureatesTable.birthDate] = laureate.birthDate
                    it[LaureatesTable.birthPlace] = laureate.birthPlace
                }
            }
        }
    }

    override suspend fun savePrizes(prizes: List<Prize>) = newSuspendedTransaction {
        for (prize: Prize in prizes) {
            savePrize(prize)
        }
    }

    override suspend fun addToFavorites(userId: Int, prizeId: Int) = newSuspendedTransaction {
        UserPrizesTable.insert {
            it[UserPrizesTable.userId] = userId
            it[UserPrizesTable.prizeId] = prizeId
            it[UserPrizesTable.addedAt] = System.currentTimeMillis()
        }
        // Явно возвращаем Unit
        Unit
    }

    override suspend fun removeFromFavorites(userId: Int, prizeId: Int) = newSuspendedTransaction {
        UserPrizesTable.deleteWhere {
            (UserPrizesTable.userId eq userId) and (UserPrizesTable.prizeId eq prizeId)
        }
        Unit
    }

    override suspend fun getUserFavorites(userId: Int): List<Prize> = newSuspendedTransaction {
        (UserPrizesTable innerJoin PrizesTable)
            .selectAll()
            .where { UserPrizesTable.userId eq userId }
            .map { rowToPrize(it) }
    }

    override suspend fun isFavorite(userId: Int, prizeId: Int): Boolean = newSuspendedTransaction {
        UserPrizesTable
            .selectAll()
            .where { (UserPrizesTable.userId eq userId) and (UserPrizesTable.prizeId eq prizeId) }
            .count() > 0
    }

    private fun rowToPrize(row: ResultRow): Prize {
        val prizeId = row[PrizesTable.id]
        val laureates = LaureatesTable
            .selectAll()
            .where { LaureatesTable.prizeId eq prizeId }
            .map { laureateRow: ResultRow ->
                Laureate(
                    id = laureateRow[LaureatesTable.id],
                    fullName = laureateRow[LaureatesTable.fullName],
                    motivation = laureateRow[LaureatesTable.motivation],
                    portion = laureateRow[LaureatesTable.portion],
                    birthDate = laureateRow[LaureatesTable.birthDate],
                    birthPlace = laureateRow[LaureatesTable.birthPlace]
                )
            }

        return Prize(
            id = prizeId,
            awardYear = row[PrizesTable.awardYear],
            category = row[PrizesTable.category],
            prizeAmount = row[PrizesTable.prizeAmount],
            dateAwarded = row[PrizesTable.dateAwarded],
            laureates = laureates
        )
    }
}