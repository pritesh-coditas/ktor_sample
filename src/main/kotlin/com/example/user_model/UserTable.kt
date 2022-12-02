package com.example.user_model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val userId:Column<String> = varchar("userId", length = 512)
    val userName:Column<String> = varchar("userName", length = 512)
    val userMobile:Column<String> = varchar("userMobile", length = 512)
    val userEmail:Column<String> = varchar("userEmail", length = 512).uniqueIndex()
    val userPassword:Column<String> = varchar("userPassword", length = 512)
    override val primaryKey:PrimaryKey = PrimaryKey(userId)
}