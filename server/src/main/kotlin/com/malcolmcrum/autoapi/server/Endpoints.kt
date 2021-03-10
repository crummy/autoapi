package com.malcolmcrum.autoapi.server

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
annotation class Name(val name: String)

class Endpoints {
    companion object {
        private val endpoints = mutableSetOf<Endpoint>()
        fun add(endpoint: Endpoint) = endpoints.add(endpoint)
        fun get(): Set<Endpoint> = endpoints
    }
}


data class Endpoint(val method: HttpMethod, val location: KClass<*>, val request: KClass<*>?, val response: KClass<*>) {
    val locationAnnotation = location.annotations.filterIsInstance<Location>().firstOrNull() ?: throw Error("Class ${location::class} is missing @Location annotation")
    val functionName = location.annotations.filterIsInstance<Name>().firstOrNull()?.name ?: "${method.value.toLowerCase()}${location.simpleName}"
    val parameters: List<KParameter> = location.constructors.first().parameters
    val returnType = response
    val path = locationAnnotation.path

    companion object {
        fun get(location: KClass<*>, response: KClass<*>) = Endpoint(HttpMethod.Get, location, null, response)
        fun post(location: KClass<*>, request: KClass<*>, response: KClass<*>) = Endpoint(HttpMethod.Post, location, request, response)
    }
}

// inline fun <reified IN : Any, reified OUT : Any> Route.get(noinline body: suspend PipelineContext<Unit, ApplicationCall>.(IN) -> Unit): Route {
inline fun <reified LOCATION : Any, reified OUT : Any> Route.get(noinline body: suspend PipelineContext<Unit, ApplicationCall>.(LOCATION) -> OUT): Route {
    println("location: ${LOCATION::class}, annotations: ${LOCATION::class.annotations.filterIsInstance<Location>().first()}")
    val endpoint = Endpoint.get(LOCATION::class, OUT::class)
    Endpoints.add(endpoint)
    return location(LOCATION::class) {
        method(HttpMethod.Get) {
            handle(LOCATION::class) {
                val response = body(this, application.locations.resolve(call))
                call.respond(response)
            }
        }
    }
}

inline fun <reified LOCATION : Any, reified IN : Any, reified OUT : Any> Route.post(noinline body: suspend PipelineContext<Unit, ApplicationCall>.(LOCATION, IN) -> OUT): Route {
    Endpoints.add(Endpoint.post(LOCATION::class, IN::class, OUT::class))
    return location(LOCATION::class) {
        method(HttpMethod.Post) {
            handle(LOCATION::class) {
                val response = body(this, application.locations.resolve(call), call.receive())
                call.respond(response)
            }
        }
    }
}


