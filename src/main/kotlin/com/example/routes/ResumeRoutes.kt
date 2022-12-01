package com.example.routes

import com.example.repository.ResumeRepository
import com.example.resume_model.Resume
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.resumeRouting(db: ResumeRepository) {
    route("/resume") {

        get {
            /*       if(resumeList.isNotEmpty()){
                       call.respond(resumeList)
                   }else{
                       call.respondText("No resume found", status = HttpStatusCode.OK)
                   }*/
            val resumeList = db.getAllResume()
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
            /*val resume = resumeList.find{it.userId.equals(userId)}?: return@get call.respondText("No resume with this Id $userId", status = HttpStatusCode.NotFound)
             call.respond(resume)*/

            val resume = db.getResumeByUserId(userId)
            call.respond("$resume")
            if (resume != null) {
                call.respond(status = HttpStatusCode.OK, resume)
            } else {
                call.respondText("No resume with this Id $userId", status = HttpStatusCode.NotFound)
            }
        }

        post {
            val resume = call.receive<Resume>()

            db.insert(resume.userId, resume.userName, resume.userMobile)

            call.respondText("Resume stored Successfully", status = HttpStatusCode.Created)
        }

        delete("{resumeId?}") {
            val resumeId = call.parameters["resumeId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            /*            if (resumeList.removeIf { it.resumeId.equals(resumeId) }) {
                            call.respondText("Resume removed", status = HttpStatusCode.Accepted)
                        } else {
                            call.respondText("Not Found", status = HttpStatusCode.NotFound)
                        }*/

            val result = db.deleteByResumeId(resumeId.toInt())
            if (result == 1) {
                call.respondText("Resume deleted", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

        patch("{resumeId?}") {
            val resumeId = call.parameters["resumeId"] ?: return@patch call.respond(HttpStatusCode.BadRequest)

            val resume = call.receive<Resume>()

            val result = db.update(resume.userId, resumeId.toInt(), resume.userName, resume.userMobile)
            if (result == 1) {
                call.respondText("Resume updated at $resumeId", status = HttpStatusCode.OK)
            } else {
                call.respondText("No resume for index $resumeId", status = HttpStatusCode.NotFound)
            }
        }
    }
}


