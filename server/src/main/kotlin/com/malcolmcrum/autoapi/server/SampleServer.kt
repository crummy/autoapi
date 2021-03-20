package com.malcolmcrum.autoapi.server

import com.malcolmcrum.autoapi.generator.Endpoints
import com.malcolmcrum.autoapi.generator.generateClient
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
    val restaurantService = RestaurantService()
    routing {
        routing(restaurantService)
    }
    generateClient(Endpoints.get())
    println("Server is started")
}