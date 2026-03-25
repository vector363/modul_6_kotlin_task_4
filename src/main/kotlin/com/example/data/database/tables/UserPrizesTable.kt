package com.example.data.database.tables

import org.jetbrains.exposed.sql.Table

object UserPrizesTable : Table("user_prizes") {
    val userId = integer("user_id").references(UsersTable.id)
    val prizeId = integer("prize_id").references(PrizesTable.id)
    val addedAt = long("added_at")

    override val primaryKey = PrimaryKey(userId, prizeId)
}