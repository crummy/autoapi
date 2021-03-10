package com.malcolmcrum.autoapi.server

import com.malcolmcrum.autoapi.generator.ClientGenerator
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(Locations)
    install(CORS) {
        anyHost()
    }
    routing {
        routing()
    }
    ClientGenerator(Endpoints.get())
}