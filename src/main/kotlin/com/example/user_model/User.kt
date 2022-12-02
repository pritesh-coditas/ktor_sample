package com.example.user_model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    var userId:String,
    var userName:String,
    var userMobile:String,
    var userEmail:String,
    var userPassword:String
):Principal