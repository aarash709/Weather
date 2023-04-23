# About
Work in progress 🚧 android Weather app written entirely in jetpack compose ui kit. This app is using best practices offered by google.
## Architecture
The app is using hybrid model for modules which is the best for managing code and readability.
`app` module is responsible for managing navigation.
`Feature` modules are compose ui pages and VMs and navigation setup for each page.
`Core` folder has separate modules for networking and offline cache.
For background tasks `Work manager` library is used. 
## Stack
UI: [Jetpack Compose]()
Database: [Room database]()
Networking: [Retrofit]()
Serialization: [Kotlinx.Serialization]()
DI: [DaggerHilt]