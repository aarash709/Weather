# Weather Sample

Work in progress [🚧] sample android weather app written entirely in Kotlin and jetpack compose. The app is fully functional and following offline first best practices. This is a demo and is not a production ready app[⚠️]. Contributions are welcomed🫡.

## Roadmap

| Active | Backlog |
|--------|---------|
| Forecast background colors(day,night,dawn,cloudy, etc..) | Animated weather condition shader effects |

## Architecture

This project is leveraging hybrid model architecture(by layer + by feature)
* `app` primarily managing navigation logic.
* `Feature` Ui code and navigation setup for each page.
* `Core` has separate modules for networking and offline cache and shared code across application.
* `WorkManager` for fetching remote data in the background.

## Stack

* UI: [Jetpack Compose](https://developer.android.com/jetpack/compose?gclid=CjwKCAiAvdCrBhBREiwAX6-6UogmclLihuJq1CXQKPCG3q9b2vidq9mAjzYjtyXgOhLz34GKmeo7-hoCh7QQAvD_BwE&gclsrc=aw.ds)
* Database: [Room database](https://developer.android.com/training/data-storage/room)
* Networking: [Ktor](https://ktor.io/)
* Serialization: [Kotlinx.Serialization](https://kotlinlang.org/docs/serialization.html)
* DI: [DaggerHilt](https://developer.android.com/training/dependency-injection/hilt-android)

## Data

Data is provided by [Open-Meteo](https://open-meteo.com/). No key is required for non-commercial use.
`BASE_URL` and parameters for daily,hourly and current are stored in [secrets.default.properties](https://github.com/aarash709/Weather/tree/master#:~:text=secrets.default.properties) and `local.properties`. Gradle secrets plugin is handling access to properties.

For AGP 8.1 an up, you need to set buildConfig to `true` in your module`s build.gradle.kts file like this:
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
Copy files in `git-hooks/*.sh` to `.git` directory to enable pre commit and pre push checks.

# License

**Weather Sample** is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
