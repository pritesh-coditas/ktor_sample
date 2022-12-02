package com.example.resume_model

import kotlinx.serialization.Serializable

@Serializable
data class Resume(
    var userId:String="",
    var resumeId:Int=0,
    var userName: String,
    var userMobile: String
)

