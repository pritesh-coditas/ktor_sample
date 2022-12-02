package com.example

import com.example.auth.JwtService
import com.example.auth.UserSession
import com.example.repository.DatabaseFactory
import com.example.repository.ResumeRepository
import com.example.repository.UserRepository
import com.example.routes.resumeRouting
import com.example.routes.userRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        DatabaseFactory.init()
        val resumeRepository = ResumeRepository()
        val userRepository = UserRepository()
        val jwt = JwtService()
        val hash = {s:String->s}
        install(Sessions) {
            cookie<UserSession>("USER_SESSION") {
                cookie.extensions["SameSite"] = "lax" // doubt
            }
        }

        install(Authentication) {
            jwt("jwt") {
                verifier(jwt.verifier)
                realm = "Todo Server"
                validate {
                    val payload = it.payload
                    val claim = payload.getClaim("userId")
                    val claimString = claim.asInt()
                    val user = userRepository.getUserByUserId(claimString.toString())
                    user
                }
            }
        }

        routing {
            resumeRouting(resumeRepository,userRepository)
            userRouting(userRepository,resumeRepository,jwt,hash)
        }
    }
        .start(wait = true)
}

/*// SSL
fun main(args:Array<String>):Unit{

    val keyStoreFile = File("build/keystore.jks")
    val keystore = generateCertificate(
        file = keyStoreFile,
        keyAlias = "sampleAlias",
        keyPassword = "pritesh",
        jksPassword = "pritesh"
    )

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keystore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "pritesh".toCharArray() },
            privateKeyPassword = { "pritesh".toCharArray() }) {
            port = 1234
            keyStorePath = keyStoreFile
        }
        module(Application::module)
    }

    //EngineMain.main(args)
    embeddedServer(Netty,environment)
        .start(wait = true)

}*/

fun Application.configureSerialization() {
    routing {
        install(ContentNegotiation) {
            json()
        }
    }
}


