package com.malcolmcrum.autoapi.client

typealias JsMap<K, V> = Map<K, V>

external class Map<in K : Any, V> {
    fun clear()
    fun delete(key: K): Boolean
    operator fun get(key: K): V?
    fun has(key: K): Boolean
    operator fun set(key: K, value: V): Map<K, V>

    val size: Int

    @Suppress("TYPE_VARIANCE_CONFLICT")
    fun forEach(action: (value: V, key: K, map: Map<K, V>) -> Unit)
}