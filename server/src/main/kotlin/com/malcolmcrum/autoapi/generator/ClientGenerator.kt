package com.malcolmcrum.autoapi.generator

import com.malcolmcrum.autoapi.server.Endpoint
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import io.ktor.client.*
import kotlinx.coroutines.GlobalScope
import java.nio.file.Paths

class ClientGenerator(responseMapping: Set<Endpoint>) {
    val jsExport = ClassName("kotlin.js", "JsExport")
    val jsonFeature = ClassName("io.ktor.client.features.json", "JsonFeature")
    val clientField = PropertySpec.builder("client", HttpClient::class, KModifier.PRIVATE)
        .initializer("HttpClient { install (%T) }", jsonFeature)
        .build()
    val constructor = FunSpec.constructorBuilder()
        .addParameter(
            ParameterSpec.builder("basePath", String::class).defaultValue("\"\"").build()
        ).build()
    val basePathProperty = PropertySpec.builder("basePath", String::class, KModifier.PRIVATE)
        .initializer("basePath")
        .build()
    val file = FileSpec.builder("", "Client")
        .addComment("This file was generated by the AutoAPI tool. Do not modify.")
        .addType(
            TypeSpec.classBuilder("AutoAPI")
                .primaryConstructor(constructor)
                .addProperty(clientField)
                .addProperty(basePathProperty)
                .addFunctions(responseMapping.map { it.toClientRequest() })
                .addAnnotation(jsExport)
                .build()
        ).build()

    val clientFolder = Paths.get("../client/src/main/kotlin")

    init {
        file.writeTo(clientFolder)
        file.writeTo(System.out)
    }

}

private fun Endpoint.toClientRequest(): FunSpec {
    val function = FunSpec.builder(functionName)
    val promise = MemberName("kotlinx.coroutines", "promise")
    val method = MemberName("io.ktor.client.request", this.method.value.toLowerCase())
    for (parameter in parameters) {
        function.addParameter(parameter.name!!, parameter.type.asTypeName())
    }
    var location = path;
    for (param in parameters) {
        location = location.replace("{${param.name}}", "\$${param.name}")
    }
    function.addStatement("val location = \"\$basePath$location\"")
    function.addStatement("return %T.%M { client.%M(location) }", GlobalScope::class, promise, method)
    val jsPromise = ClassName("kotlin.js", "Promise")
    val returns = ClassName(returnType.qualifiedName!!.removePrefix(returnType.simpleName!!), returnType.simpleName!!)
    function.returns(jsPromise.parameterizedBy(returns))
    return function.build()
}