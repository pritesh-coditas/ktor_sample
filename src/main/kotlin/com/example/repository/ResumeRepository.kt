package com.example.repository

import com.example.resume_model.Resume
import com.example.resume_model.ResumeTable
import com.example.resume_model.dao.ResumeDao
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class ResumeRepository : ResumeDao {

    override suspend fun insert(
        userId: String,
        userName: String,
        userMobile: String
    ): Resume? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = ResumeTable.insert { resume ->
                resume[ResumeTable.userId] = userId
                resume[ResumeTable.userName] = userName
                resume[ResumeTable.userMobile] = userMobile
            }
        }
        return rowToResume(statement?.resultedValues?.get(0))
    }

    override suspend fun getAllResume(): List<Resume> =
        DatabaseFactory.dbQuery {
            ResumeTable.selectAll().mapNotNull {
                rowToResume(it)
            }
        }

    override suspend fun getResumeByUserId(userId: String): List<Resume> =
        DatabaseFactory.dbQuery {
            ResumeTable.select{
                ResumeTable.userId.eq(userId)
            }.mapNotNull {
                rowToResume(it)
            }
        }

    override suspend fun deleteByUserId(userId: String): Int =
        DatabaseFactory.dbQuery {
            ResumeTable.deleteWhere { ResumeTable.userId.eq(userId) }
        }

    override suspend fun deleteByResumeId(resumeId: Int): Int =
        DatabaseFactory.dbQuery {
            ResumeTable.deleteWhere { ResumeTable.resumeId.eq(resumeId) }
        }

    override suspend fun updateResume(userId: String, resumeId: Int, userName: String, userMobile: String): Int =
        DatabaseFactory.dbQuery {
            ResumeTable.update({ ResumeTable.resumeId.eq(resumeId) }) { resume ->
                resume[ResumeTable.userId] = userId
                resume[ResumeTable.userName] = userName
                resume[ResumeTable.userMobile] = userMobile
            }
        }

    private fun rowToResume(row: ResultRow?): Resume? {
        if (row == null) return null
        return Resume(
            userId = row[ResumeTable.userId],
            resumeId = row[ResumeTable.resumeId],
            userName = row[ResumeTable.userName],
            userMobile = row[ResumeTable.userMobile]
        )
    }
}