import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
data class Body(val foo: String)

@Serializable
@JsExport
data class SampleResponse(val foo: String)