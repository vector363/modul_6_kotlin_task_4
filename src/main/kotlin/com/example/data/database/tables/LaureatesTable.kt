package com.example.data.database.tables

import org.jetbrains.exposed.sql.Table

object LaureatesTable : Table("laureates") {
    val id = varchar("id", 50)
    val prizeId = integer("prize_id").references(PrizesTable.id)
    val fullName = varchar("full_name", 255)
    val motivation = text("motivation")
    val portion = varchar("portion", 10)
    val birthDate = varchar("birth_date", 20).nullable()
    val birthPlace = varchar("birth_place", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}