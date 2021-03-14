package com.malcolmcrum.autoapi.server

import io.ktor.locations.*

@Location("/restaurants")
class GetRestaurants

@Location("/restaurants")
class CreateRestaurant

@Location("/restaurants/{id}")
data class GetRestaurant(val id: Int)