# Weather Sample

Work in progress [🚧] sample android weather app written entirely in Kotlin and jetpack compose. The app is fully functional and following offline first best practices. This is not a production ready app[⚠️]. Contributions are welcomed🫡.

## Roadmap

* [ ] Redesign Forecast page
* [ ] Redesign pullrefresh
* [ ] Add background to Forecast page
* [ ] Data Sync strategy
* [x] Test module
* [x] Add testing for core layer and feature modules

## Data

Data is provided by [OpenWeatherMap V2.5](https://openweathermap.org/api).
To get started and make the app work, you need to get a key. Go to the [OpenWeatherMap](https://openweathermap.org/api) and generate a key(which will be a v2.5 an NOT V3.0) then copy the key to `local.properties` and then past it for the `API_KEY` property. Sync the gradle and you are good to go.
Your `local.properties` file shoud have the following proprties:

```gradle
BASE_URL=https://api.openweathermap.org/
API_KEY=[GeneratedApiKey]
```
To access to these properties and if you are using AGP 8.1 an up, you need to set buildConfig to `true` in your module`s build.gradle.kts file like this:
```gradle
android{

  buildFeatures {
    buildConfig = true
  }
}
```

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

# License

**Weather Sample** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
