package com.example.repository

import com.example.resume_model.ResumeTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(ResumeTable)
        }
    }

    private fun hikari(): HikariDataSource {

        val config = HikariConfig()
        config.driverClassName = System.getenv("JDBC_DRIVER") // post_gre driver.
        config.jdbcUrl = System.getenv("JDBC_DATABASE_URL") // url for post_gre database.
         config.maximumPoolSize = 3 // number of database connections between database and application.
        config.isAutoCommit = false // start new transaction after each commit.
        config.transactionIsolation =
            "TRANSACTION_REPEATABLE_READ" // same data for B transaction for 2nd time even after data changed by transaction A.

        return HikariDataSource(config)
    }

    //doubt
    suspend fun<T> dbQuery(block: ()->T):T =
        withContext(Dispatchers.IO){
            transaction {
                block()
            }
        }

}