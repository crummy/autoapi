package com.malcolmcrum.autoapi.server

import Body
import SampleResponse
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.routing() {
    get<Listing, SampleResponse> { listing ->
        val response = SampleResponse(listing.name)
        println(response)
        return@get response
    }

    post<Listing, Body, SampleResponse> { listing: Listing, body: Body ->
        val response = SampleResponse(listing.name)
        println(response)
        return@post response
    }
}