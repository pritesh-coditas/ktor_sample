package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.resumeData(){
    routing{
        get("/resumedata"){
            call.respond("pritesh")
        }
        get("/data/{page}"){
            val pageNumber  = call.parameters["page"]
            call.respond("$pageNumber")
        }
    }
}