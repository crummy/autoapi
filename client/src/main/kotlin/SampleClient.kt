import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.Promise

@JsExport
class SampleClient {
    private val client = HttpClient {
        install(JsonFeature)
    }

    fun listing(name: String, page: Int): Promise<Restaurant> {
        //val url = "location/{name}/{page}".replace("{name}", name).replace("{page}", page.toString())
        val url = "http://example.com"
        return GlobalScope.promise {
            client.get(url)
        }
    }
}