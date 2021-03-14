package com.malcolmcrum.autoapi.server

import NewRestaurant
import Restaurant

class RestaurantService {
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
        return restaurant
    }

    fun setDelivery(id: Int, available: Boolean): Restaurant? {
        val restaurant = restaurants[id]
        if (restaurant != null) {
            restaurants[id] = restaurant.copy(delivery = available)
        }
        return restaurants[id];
    }

}