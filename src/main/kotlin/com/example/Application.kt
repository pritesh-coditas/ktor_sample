package com.example

import com.example.plugins.resumeData
import com.example.repository.DatabaseFactory
import com.example.repository.ResumeRepository
import com.example.routes.resumeRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSerialization()
        module()
      //  configureRouting()
        DatabaseFactory.init()
        val db = ResumeRepository()
        routing {
            resumeRouting(db)
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

fun Application.module() {
    resumeData()
}

fun Application.configureRouting() {
    routing {
       // resumeRouting()
    }
}

fun Application.configureSerialization(){
    routing{
        install(ContentNegotiation){
            json()
        }
    }
}

fun Application.configureDataBase(){
    DatabaseFactory.init()
}

