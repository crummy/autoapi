package com.malcolmcrum.autoapi.server

import NewRestaurant
import Restaurant
import org.slf4j.LoggerFactory

class RestaurantService {
    private val log = LoggerFactory.getLogger(RestaurantService::class.java)

    private val restaurants = mutableMapOf<Int, Restaurant>()

    fun getAll(): List<Restaurant> {
        return restaurants.values.sortedBy { it.id }
    }

    fun get(id: Int): Restaurant? {
        return restaurants[id]
    }

    fun create(new: NewRestaurant): Restaurant {
        val id = restaurants.keys.maxOrNull() ?: 1
        val restaurant = Restaurant(id, new.name, new.address, new.delivery)
        restaurants[id] = restaurant
        log.info("Created new restaurant $restaurant")
        return restaurant
    }

    fun setDelivery(id: Int, available: Boolean): Restaurant? {
        val restaurant = restaurants[id]
        return if (restaurant != null) {
            restaurants[id] = restaurant.copy(delivery = available)
            log.info("Set delivery for restaurant $id $ to $available")
            restaurant
        } else {
            log.warn("Tried to set delivery for restaurant $id but none was found")
            null;
        }
    }

}