import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
data class Body(val foo: String)

@Serializable
@JsExport
data class Restaurant(val id: Int, val name: String, val address: String, val delivery: Boolean = false)

@Serializable
@JsExport
// TODO: is there a better pattern here to reduce repetition?
data class NewRestaurant(val name: String, val address: String, val delivery: Boolean = false)