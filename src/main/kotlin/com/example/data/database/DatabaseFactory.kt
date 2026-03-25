package com.example.data.database

import com.example.data.database.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://ep-red-grass-anprvmu7.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require"
            username = "neondb_owner"
            password = "npg_0OpVfUetZF4l"
            driverClassName = "org.postgresql.Driver"

            // Настройки пула для Neon
            maximumPoolSize = 10
            minimumIdle = 2
            connectionTimeout = 30000  // 30 секунд
            maxLifetime = 1800000      // 30 минут
            idleTimeout = 300000       // 5 минут

            // Дополнительные параметры для Neon
            addDataSourceProperty("socketTimeout", "30")
            addDataSourceProperty("tcpKeepAlive", "true")
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        // Создаем таблицы
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                UsersTable,
                PrizesTable,
                LaureatesTable,
                UserPrizesTable
            )
            println("Database tables created/verified")
        }
        runBlocking {
            DataInitializer.initializeData()
        }
    }


}