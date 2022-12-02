package com.example.repository

import com.example.user_model.User
import com.example.user_model.UserTable
import com.example.user_model.dao.UserDao
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserRepository :UserDao {

    override suspend fun createUser(
        userId: String,
        userName: String,
        userMobile: String,
        userEmail: String,
        userPassword: String
    ): User? {
        var statement: InsertStatement<Number>? = null
        DatabaseFactory.dbQuery {
            statement = UserTable.insert { user->
                user[UserTable.userId] = userId
                user[UserTable.userName] = userName
                user[UserTable.userMobile] = userMobile
                user[UserTable.userEmail] = userEmail
                user[UserTable.userPassword] = userPassword
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun getUserByUserId(userId: String): User? =
        DatabaseFactory.dbQuery {
            UserTable.select{
                UserTable.userId.eq(userId)
            }.mapNotNull {
                rowToUser(it)
            }.singleOrNull()
    }

    override suspend fun deleteUser(userId: String): Int  =
        DatabaseFactory.dbQuery {
            UserTable.deleteWhere { UserTable.userId.eq(userId) }
        }

    override suspend fun updateUser(
        userId: String,
        userName: String,
        userMobile: String,
        userEmail: String,
        userPassword: String
    ): Int =
        DatabaseFactory.dbQuery {
            UserTable.update({UserTable.userId.eq(userId)}){user->
                user[UserTable.userName] = userName
                user[UserTable.userMobile] = userMobile
                user[UserTable.userEmail] = userEmail
                user[UserTable.userPassword] = userPassword
            }
        }

    private fun rowToUser(row: ResultRow?): User?{
        if(row==null) return null
        return User(
            userId = row[UserTable.userId],
            userName = row[UserTable.userName],
            userMobile = row[UserTable.userMobile],
            userEmail = row[UserTable.userEmail],
            userPassword = row[UserTable.userPassword]
        )
    }

}