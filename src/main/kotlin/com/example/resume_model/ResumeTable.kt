package com.example.resume_model

import com.example.user_model.UserTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object ResumeTable : Table() {
    val resumeId:Column<Int> = integer("resumeId").autoIncrement()
    val userId:Column<String> = varchar("userId", length = 512).references(UserTable.userEmail)
    val userName:Column<String> = varchar("userName", length = 512)
    val userMobile:Column<String> = varchar("userMobile", length = 512)

    override val primaryKey: PrimaryKey = PrimaryKey(resumeId)
}