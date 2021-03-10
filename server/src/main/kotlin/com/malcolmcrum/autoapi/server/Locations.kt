package com.malcolmcrum.autoapi.server

import io.ktor.locations.*

@KtorExperimentalLocationsAPI
@Location("/list/{name}/page/{page}")
data class Listing(val name: String, val page: Int)
