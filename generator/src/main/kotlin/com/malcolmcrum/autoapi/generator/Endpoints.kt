package com.malcolmcrum.autoapi.generator

import com.malcolmcrum.autoapi.JsMap
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
annotation class Name(val name: String)

class Endpoints {
    companion object {
        private val endpoints = mutableSetOf<Endpoint>()
        fun add(endpoint: Endpoint) = endpoints.add(endpoint)
        fun get(): Set<Endpoint> = endpoints
    }

}

data class Endpoint(val method: HttpMethod, val location: KClass<*>, val request: KType?, val response: KType) {
    val locationAnnotation = location.annotations.filterIsInstance<Location>().firstOrNull()
        ?: throw Error("Class ${location::class} is missing @Location annotation")
    val functionName =
        location.annotations.filterIsInstance<Name>().firstOrNull()?.name ?: location.simpleName!!.decapitalize()
    val parameters: List<KParameter> = location.constructors.first().parameters
    val requestBody = request?.asTypeName()?.overrideTypes()
    val returnType = response.asTypeName().overrideTypes()
    val path = locationAnnotation.path

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        inline fun <reified LOCATION : Any, reified OUT : Any> get(): Endpoint {
            return Endpoint(HttpMethod.Get, LOCATION::class, null, typeOf<OUT>())
        }

        @OptIn(ExperimentalStdlibApi::class)
        inline fun <reified LOCATION : Any, reified IN : Any, reified OUT : Any> post(): Endpoint {
            return Endpoint(HttpMethod.Post, LOCATION::class, typeOf<IN>(), typeOf<OUT>())
        }
    }
}

// Collections and Lists are non-exportable. Use Array instead.
// And Records (aka JS objects) aren't either? So use a custom Record type for now (awkward, I know)
private fun TypeName.overrideTypes(): TypeName {
    if (this is TypeVariableName) {
        if (name == "Collection" || name == "List") {
            return TypeVariableName("Array", bounds, variance)
        }
    } else if (this is ParameterizedTypeName) {
        if (rawType == ClassName("kotlin.collections", "List")
            || rawType == ClassName("kotlin.collections", "Collection")
        ) {
            return ClassName("kotlin", "Array").parameterizedBy(typeArguments.map { it.overrideTypes() })
        }
        else if (rawType == Map::class.asClassName()) {
            return JsMap::class.asClassName()
        }
    }
    return this;
}

@ExperimentalStdlibApi
inline fun <reified LOCATION : Any, reified OUT : Any> Route.get(noinline body: suspend PipelineContext<Unit, ApplicationCall>.(LOCATION) -> OUT): Route {
    val endpoint = Endpoint.get<LOCATION, OUT>()
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
    val endpoint = Endpoint.post<LOCATION, IN, OUT>()
    Endpoints.add(endpoint)
    return location(LOCATION::class) {
        method(HttpMethod.Post) {
            handle(LOCATION::class) {
                val response = body(this, application.locations.resolve(call), call.receive())
                call.respond(response)
            }
        }
    }
}

data class Record<T, U>(val from: T, val to: U)
