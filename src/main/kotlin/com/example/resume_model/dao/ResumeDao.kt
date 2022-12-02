package com.example.resume_model.dao

import com.example.resume_model.Resume

interface ResumeDao {

    suspend fun insert(
        userId:String,
        userName:String,
        userMobile:String
    ):Resume?

   suspend fun getAllResume():List<Resume>?

   suspend fun getResumeByUserId(userId:String):List<Resume>?

   suspend fun deleteByUserId(userId:String):Int
   suspend fun deleteByResumeId(resumeId:Int):Int

   suspend fun updateResume(
       userId: String,
       resumeId: Int,
       userName: String,
       userMobile: String
   ):Int
}