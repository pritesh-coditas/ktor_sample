package com.example.routes

import com.example.auth.JwtService
import com.example.auth.UserSession
import com.example.repository.ResumeRepository
import com.example.repository.UserRepository
import com.example.user_model.User
import com.example.user_model.UserLogin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.userRouting(
    userDb: UserRepository,
    resumeDb: ResumeRepository,
    jwtService: JwtService,
    hash: (String) -> String
) {
    route("/user") {
        // get user by id
        get("{userId?}") {
            val userId = call.parameters["userId"] ?: return@get call.respondText(
                "Missing Id",
                status = HttpStatusCode.BadRequest
            )
            val currentUser = userDb.getUserByUserId(userId)
            if (currentUser != null) {
                call.respond(status = HttpStatusCode.OK, currentUser)
            } else {
                call.respondText("No user with this Id $userId", status = HttpStatusCode.NotFound)
            }
        }

        // insert user
        post("/register") {
            val parameter = call.receive<User>()

            val hashPassword = hash(parameter.userPassword)
            val currentUser = userDb.createUser(
                parameter.userId,
                parameter.userName,
                parameter.userMobile,
                parameter.userEmail,
                hashPassword
            )
            try {
                currentUser?.userId?.let {
                    call.sessions.set(UserSession(it))
                    call.respondText(
                        jwtService.generateToken(currentUser),
                        status = HttpStatusCode.Created
                    )
                }
            } catch (e: Throwable) {
                call.respondText("problem while creating user")
            }
        }

        // login user with userId and Password
        post("/login") {
            val parameter = call.receive<UserLogin>()

            val userEmail = parameter.userEmail
            val userPassword = parameter.userPassword

            val hashPassword = hash(userPassword)

            try {
                val currentUser = userDb.getUserByUserId(userEmail)
                if (currentUser == null) {
                    call.respondText("No user found")
                }

                currentUser?.userId?.let {
                    if (currentUser.userPassword == hashPassword) {
                        call.sessions.set(UserSession(it))
                        call.respondText(jwtService.generateToken(currentUser))
                    } else {
                        call.respond(status = HttpStatusCode.BadRequest, "problem to retrieve user")
                    }
                }
            } catch (e: Throwable) {
                call.respondText("problem while creating user")
            }
        }

        // delete use with userId
        delete("{userId}") {
            val userId = call.parameters["userId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val user = call.sessions.get<UserSession>()?.let {
                userDb.getUserByUserId(it.userId)
            }
            if (user == null) {
                call.respondText("User Not found", status = HttpStatusCode.BadRequest)
            }
            try {
                resumeDb.deleteByUserId(userId)
                val currentUser = user?.userId?.let { it1 -> userDb.deleteUser(it1) }
                if (currentUser == 1) {
                    call.respondText("User deleted with his resumes")
                } else {
                    call.respondText("getting problem to delete user")
                }
            } catch (e: Throwable) {
                call.respondText("Some error while deleting")
            }
        }

        // update
        put {
            val parameter = call.receive<User>()
            val user = call.sessions.get<UserSession>()?.let {
                userDb.getUserByUserId(it.userId)
            }
            val hashPassword = hash(parameter.userPassword)
            try {
                val currentUser = user?.userId?.let { it1 ->
                    userDb.updateUser(
                        it1,
                        parameter.userName,
                        parameter.userMobile,
                        parameter.userEmail,
                        hashPassword
                    )
                }

                if (currentUser == 1) {
                    call.respondText("updated successfully")
                } else {
                    call.respondText("getting problem $currentUser")
                }
            } catch (e: Exception) {
                call.respondText("getting problem")
            }
        }
    }
}