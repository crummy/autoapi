# autoapi
An example of how to autogenerate an API for a Kotlin backend, and call it from JS. Requires a fair bit
of experimental Kotlin functionality, not all of which works (e.g. serializing data classes from the frontend).
Serves as a POC using Ktor.

## The backend

Routes are defined like so, using the Locations feature in Ktor:

```
@Location("/restaurants")
class GetRestaurants

@Location("/restaurants")
class CreateRestaurant

@Location("/restaurants/{id}")
data class GetRestaurant(val id: Int)
```

They are implemented using special typed route handlers:

```    
// First parameter, GetRestaurants, is the location, last parameter is the return type
get<GetRestaurants, List<Restaurant>> {
    return@get restaurantService.getAll()
}

// First parameter is the location, which is a data class containing a restaurant ID, passed in as a
// parameter to the handler.
// Again the last parameter is the return type.
get<GetRestaurant, Restaurant> { singleRestaurant ->
    val id = singleRestaurant.id
    return@get restaurantService.get(id) ?: throw NotFoundException("Restaurant $id does not exist")
}

// For POST requests, the second parameter is the body of the request.
post<CreateRestaurant, NewRestaurant, Restaurant> { _, newRestaurant ->
    return@post restaurantService.create(newRestaurant)
}
```

## Client generation

When the server is run, I examine the routes and use KotlinPoet to generate a Kotlin client to query the
backend routes defined above.

Here's a look at the generated code:
```
@JsExport
public class AutoAPI(
  private val basePath: String = ""
) {
  private val client: HttpClient = HttpClient { install (JsonFeature) }

  public fun getRestaurants(): Promise<Array<Restaurant>> {
    val location = "$basePath/restaurants"
    return GlobalScope.promise { client.`get`(location) }
  }

  public fun getRestaurant(id: Int): Promise<Restaurant> {
    val location = "$basePath/restaurants/$id"
    return GlobalScope.promise { client.`get`(location) }
  }

  public fun createRestaurant(body: NewRestaurant): Promise<Restaurant> {
    val location = "$basePath/restaurants"
    return GlobalScope.promise { client.post(location) { contentType(Json); this.body = body } }
  }
}
```

To turn this into JS (and TypeScript definitions), gradle client:build turns the Kotlin into something
web-consumable. The generated code is a bit hairy but here you can see it being used immediately in the frontend:

    const client = new AutoAPI("http://localhost:8081");
    useEffect(() => {
        client.getRestaurants()
            .then((result) => setRestaurants(result)) // the result is typed as a Restaurant[]
            .catch((error: any) => setError(error))
    }, [])
