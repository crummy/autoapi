import './App.css';
import {AutoAPI} from 'autoapi-client'
import {NewRestaurant, Restaurant} from 'autoapi-shared'
import React, {useEffect, useState} from "react";

function App() {
    const client = new AutoAPI("http://crummy:8081");
    const [restaurants, setRestaurants] = useState<Restaurant[]>([])
    const [error, setError] = useState(undefined)

    const [name, setName] = useState<string>()
    const [address, setAddress] = useState<string>()
    const [delivers, setDelivers] = useState<boolean>(false)

    useEffect(() => {
        client.getRestaurants()
            .then((result) => setRestaurants(result))
            .catch((error: any) => setError(error))

        //client.getRestaurantMap().then(result => console.log(result))
    }, [])

    const createRestaurant  = (e: React.MouseEvent<HTMLButtonElement>) => {
        client.createRestaurant(new NewRestaurant(name!!, address!!, delivers))
        e.preventDefault()
    }

    return (
        <div className="App">
            <header className="App-header">
                <h1>My Favourite Restaurants</h1>
            </header>
            {error && <h2>Error: {error}</h2>}
            {restaurants.map(restaurant =>
                <div key={restaurant.id}>
                    <span className={"name"}>{restaurant.name}</span>
                    <span className={"address"}>{restaurant.address}</span>
                    <span className={"delivers"}>{restaurant.delivery && "Delivers"}</span>
                    <span className={"delete"}><button>X</button></span>
                </div>
            )}
            <form>
                <input onChange={e => setName(e.target.value)}/>
                <input onChange={e => setAddress(e.target.value)}/>
                <input type="checkbox" onChange={e => setDelivers(e.target.checked)}/>
                <button onClick={createRestaurant}>Add Restaurant</button>
            </form>
        </div>
    );
}

export default App;
