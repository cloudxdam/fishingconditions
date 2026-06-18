# FishingConditions

Android app to check fishing conditions for predefined coastal spots in Tenerife. The app combines weather, sea state, tides, moon phase, and a custom fishing score in a single screen.

## Features

- Select a fishing spot from a predefined list.
- Choose date and hour to check the forecast.
- View temperature, wind speed and direction, sunrise, and sunset.
- View wave height and wave period.
- View next high tide and low tide.
- View moon phase.
- Get a fishing score from 0 to 100 based on the selected conditions.
- Cache tide data locally with Room to avoid unnecessary repeated requests.

## Tech Stack

- Android Views
- Java
- Gradle Kotlin DSL
- Retrofit + Gson
- OkHttp
- Room
- Material Components

## Data Sources

- Open-Meteo Weather API
- Open-Meteo Marine API
- TideCheck API

## Requirements

- Android Studio
- JDK 11
- Android SDK with `compileSdk 36`
- Internet connection for remote API calls

## Local Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Create or update `local.properties` in the project root.
4. Add the API key used by your local build:

```properties
TIDECHECK_API_KEY=your_tidecheck_api_key
```

5. Sync Gradle.
6. Run the `app` configuration on an emulator or Android device.

## Project Structure

```text
app/src/main/java/com/pachedev/fishingconditions/
|- data/
|  |- local/
|  |- network/
|  `- repository/
|- model/
|  |- domain/
|  |- marine/
|  |- tidecheck/
|  `- weather/
|- ui/
`- utils/
```

## How It Works

1. The user selects a fishing spot, date, and time.
2. The app requests weather data from Open-Meteo.
3. The app requests marine data from Open-Meteo Marine.
4. The app checks the local Room cache for tide data.
5. If there is no cached tide data, the app requests it from TideCheck and stores it locally.
6. The app calculates moon phase and fishing score.
7. The app displays all values in the main screen.

## Current Status

This project is a functional MVP with a complete end-to-end flow for forecast lookup and score calculation.

Current limitations:

- The app still needs stronger automated test coverage.
- Some error and null-data paths can be improved.
- UI state management is still activity-driven.
- The list of fishing spots is currently hardcoded.

## Roadmap

- Add unit tests for calculators and repository mapping.
- Improve null-safety and error handling.
- Move screen state to a `ViewModel`.
- Improve tide cache uniqueness and conflict handling.
- Support easier spot management and expansion.

## Notes

- `TIDECHECK_API_KEY` is required for TideCheck requests.

## License

No license specified yet.
