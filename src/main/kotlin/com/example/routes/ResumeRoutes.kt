package com.example.routes

import com.example.auth.UserSession
import com.example.repository.ResumeRepository
import com.example.repository.UserRepository
import com.example.resume_model.Resume
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.resumeRouting(resumeDb: ResumeRepository, userDb: UserRepository) {

    route("/resume") {
        get {
            val resumeList = resumeDb.getAllResume()
            if (resumeList.isNotEmpty()) {
                call.respond(status = HttpStatusCode.OK, resumeList)
            } else {
                call.respondText("No resume found", status = HttpStatusCode.NotFound)
            }
        }

        get("{userId?}") {
            val userId = call.parameters["userId"] ?: return@get call.respondText(
                "Missing Id",
                status = HttpStatusCode.BadRequest
            )
            val resume = resumeDb.getResumeByUserId(userId)
            if (resume.isNotEmpty()) {
                call.respond(status = HttpStatusCode.OK, resume)
            } else {
                call.respondText("No resume with this Id $userId", status = HttpStatusCode.NotFound)
            }
        }

        post {
            val resume = call.receive<Resume>()
            val user = call.sessions.get<UserSession>()?.let {
                userDb.getUserByUserId(it.userId)
            }
            user?.userId?.let { it1 -> resumeDb.insert(it1, resume.userName, resume.userMobile) }
            call.respondText("Resume stored Successfully", status = HttpStatusCode.Created)
        }

        delete("{resumeId?}") {
            val resumeId = call.parameters["resumeId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val result = resumeDb.deleteByResumeId(resumeId.toInt())
            if (result == 1) {
                call.respondText("Resume deleted", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        delete("{userId?}") {
            val userId = call.parameters["userId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

            val result = resumeDb.deleteByUserId(userId)
            if (result == 1) {
                call.respondText("All resume Deleted with user", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not found", status = HttpStatusCode.NotFound)
            }
        }

        patch("{resumeId?}") {
            val resumeId = call.parameters["resumeId"] ?: return@patch call.respond(HttpStatusCode.BadRequest)

            val resume = call.receive<Resume>()

            val result = resumeDb.updateResume(resume.userId, resumeId.toInt(), resume.userName, resume.userMobile)
            if (result == 1) {
                call.respondText("Resume updated at $resumeId", status = HttpStatusCode.OK)
            } else {
                call.respondText("No resume for index $resumeId", status = HttpStatusCode.NotFound)
            }
        }
    }
}


