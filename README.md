# About

Work in progress 🚧 android weather app written entirely in Kotlin and jetpack compose. The app is following offline first best practices and fully functional, but may lack some minor features.

## Roadmap

* [ ] Redesign Forecast page
* [ ] Add background to Forecast page
* [ ] Redesign pullrefresh

## Architecture

Hybrid model is used for modules which is the best for managing code and readability.
* `app` module is responsible for managing navigation.
* `Feature` modules are compose ui pages and VMs and navigation setup for each page.
* `Core` folder has separate modules for networking and offline cache and shared code across application.
* `WorkManager` For background tasks like fetching fresh weather data.

## Stack

* UI: [Jetpack Compose]()
* Database: [Room database]()
* Networking: [Retrofit]()
* Serialization: [Kotlinx.Serialization]()
* DI: [DaggerHilt]()
