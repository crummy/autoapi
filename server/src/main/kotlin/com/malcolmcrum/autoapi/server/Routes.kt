package com.malcolmcrum.autoapi.server

import NewRestaurant
import Restaurant
import com.malcolmcrum.autoapi.generator.get
import com.malcolmcrum.autoapi.generator.post
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

@OptIn(ExperimentalStdlibApi::class)
fun Route.routing(restaurantService: RestaurantService) {
    get<GetRestaurants, List<Restaurant>> {
        return@get restaurantService.getAll()
    }

    get<GetRestaurantMap, Map<Int, Restaurant>> {
        return@get mapOf()
    }

    get<GetRestaurant, Restaurant> { singleRestaurant ->
        val id = singleRestaurant.id
        return@get restaurantService.get(id) ?: throw NotFoundException("Restaurant $id does not exist")
    }

    post<CreateRestaurant, NewRestaurant, Restaurant> { _, newRestaurant ->
        return@post restaurantService.create(newRestaurant)
    }


}