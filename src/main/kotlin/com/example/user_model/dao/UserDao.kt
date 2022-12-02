package com.example.user_model.dao

import com.example.user_model.User

interface UserDao {

    suspend fun createUser(
         userId:String,
         userName:String,
         userMobile:String,
         userEmail:String,
         userPassword:String
    ):User?

    suspend fun getUserByUserId(userId:String):User?

    suspend fun deleteUser(userId: String):Int

    suspend fun updateUser(userId: String,
                           userName: String,
                           userMobile: String,
                           userEmail: String,
                           userPassword: String
                           ):Int

}