# Weather Sample

Work in progress [🚧] sample android weather app written entirely in Kotlin and jetpack compose. The app is fully functional and following offline first best practices. This is not and will not be a production ready app[⚠️]. Contributions are welcomed🫡.

## Roadmap
* [ ] Forecast background colors(day,night,dawn,cloudy, etc..)
* [ ] Reorder saved locations
#### backlog
* [ ] Add animated weather condition shader effects 

## Architecture

Hybrid model is used for modules which is the best for managing code and readability.
* `app` is responsible for managing navigation.
* `Feature` modules are compose ui pages and VMs and navigation setup for each page.
* `Core` has separate modules for networking and offline cache and shared code across application.
* `WorkManager` for fetching remote data in background.

## Stack

* UI: [Jetpack Compose](https://developer.android.com/jetpack/compose?gclid=CjwKCAiAvdCrBhBREiwAX6-6UogmclLihuJq1CXQKPCG3q9b2vidq9mAjzYjtyXgOhLz34GKmeo7-hoCh7QQAvD_BwE&gclsrc=aw.ds)
* Database: [Room database](https://developer.android.com/training/data-storage/room)
* Networking: [Retrofit](https://square.github.io/retrofit/)
* Serialization: [Kotlinx.Serialization](https://kotlinlang.org/docs/serialization.html)
* DI: [DaggerHilt](https://developer.android.com/training/dependency-injection/hilt-android)

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
## Code Analisis
This project is using static code analysis tools. Detekt and Kotlinter is used for styling and code smells. Detekt is using a `*.yml` file located at `config/detekt/config.yml`, which is currently customised to meet the project needs. Kotlinter however can be configured using `.editorconfig` file and ktlint rules.

## Git hooks
Copy files in `git-hooks/*.sh` to `.git` directory to enable pre commit and pre push checks. These files can be copied automatically using a gradle task if you will.

# License

**Weather Sample** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
