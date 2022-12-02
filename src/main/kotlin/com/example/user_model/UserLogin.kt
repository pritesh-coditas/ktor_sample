package com.example.user_model

import kotlinx.serialization.Serializable

@Serializable
data class UserLogin(
    val userEmail:String,
    val userPassword:String
)
