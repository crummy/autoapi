import logo from './logo.svg';
import './App.css';
import {AutoAPI} from 'autoapi-client'
import {SampleResponse} from 'autoapi-shared'
import {useEffect} from "react";

function App() {
    const client = new AutoAPI("http://localhost:8080");

    useEffect(() => {
        client.getListing("foo", 1)
            .then((result: SampleResponse) => console.log(result))
            .catch((error: any) => console.error(error))
    })

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload.
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
