package com.example.data.database.tables

import org.jetbrains.exposed.sql.Table

object PrizesTable : Table("prizes") {
    val id = integer("id").autoIncrement()
    val awardYear = varchar("award_year", 4)
    val category = varchar("category", 50)
    val prizeAmount = integer("prize_amount").nullable()
    val dateAwarded = varchar("date_awarded", 20).nullable()

    override val primaryKey = PrimaryKey(id)
}