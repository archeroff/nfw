# Week Checker

A Kotlin Multiplatform Mobile application that determines whether the current calendar week is **Even** or **Odd**, following the ISO-8601 standard.

## Features

- Displays current ISO-8601 week number
- Shows Even/Odd week status
- Shows current date and week range (Monday - Sunday)
- Material 3 theming with light/dark mode support
- Dynamic colors on Android (Material You)
- English and French localization
- Fully offline, no network required
- Clean Architecture with MVVM pattern

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin 2.1.0 | Language |
| Compose Multiplatform 1.7.3 | UI |
| Material 3 | Design system |
| Koin 4.0.0 | Dependency injection |
| kotlinx-datetime 0.6.1 | Date calculations |
| kotlinx-coroutines 1.9.0 | Async operations |

## Project Structure

```
WeekChecker/
├── shared/                        # Shared KMP module
│   ├── src/
│   │   ├── commonMain/kotlin/com/weekchecker/
│   │   │   ├── data/
│   │   │   │   ├── calculator/WeekCalculator.kt
│   │   │   │   ├── provider/DateProvider.kt
│   │   │   │   └── repository/WeekRepositoryImpl.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/WeekInfo.kt
│   │   │   │   ├── repository/WeekRepository.kt
│   │   │   │   └── usecase/GetCurrentWeekUseCase.kt
│   │   │   ├── presentation/
│   │   │   │   ├── model/WeekUiState.kt
│   │   │   │   ├── screen/
│   │   │   │   │   ├── WeekCheckerScreen.kt
│   │   │   │   │   └── WeekViewModel.kt
│   │   │   │   └── theme/Theme.kt
│   │   │   ├── di/AppModule.kt
│   │   │   └── util/Strings.kt
│   │   ├── commonTest/            # Shared unit tests
│   │   ├── androidMain/           # Android-specific (dynamic colors)
│   │   └── iosMain/               # iOS-specific (Koin init, entry point)
│   └── build.gradle.kts
├── androidApp/                    # Android application
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/weekchecker/
│   │   │   ├── MainActivity.kt
│   │   │   └── WeekCheckerApp.kt
│   │   └── res/
│   └── build.gradle.kts
├── iosApp/                        # iOS SwiftUI wrapper
│   └── WeekChecker/
│       └── WeekCheckerApp.swift
├── gradle/libs.versions.toml      # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Architecture

The project follows **Clean Architecture** with three layers:

### Presentation Layer
- **WeekCheckerScreen** - Compose UI with Material 3
- **WeekViewModel** - MVVM ViewModel using StateFlow
- **WeekUiState** - Sealed interface for UI states (Loading, Success, Error)

### Domain Layer
- **WeekInfo** - Data model for week information
- **GetCurrentWeekUseCase** - Business logic use case
- **WeekRepository** - Repository interface

### Data Layer
- **WeekCalculator** - ISO-8601 week number calculation
- **WeekRepositoryImpl** - Repository implementation
- **DateProvider** - Current date abstraction

## Prerequisites

- **Android**: Android Studio Ladybug (2024.2+) with JDK 17
- **iOS**: Xcode 16+ with Kotlin CocoaPods plugin
- **Both**: JDK 17

## Build & Run

### Android

```bash
cd WeekChecker
./gradlew :androidApp:assembleDebug
```

Or open `androidApp/` in Android Studio and run on emulator/device.

### iOS

1. Open `iosApp/WeekChecker.xcodeproj` in Xcode
2. Select a simulator or device
3. Build and run (Cmd+R)

Or via command line:

```bash
cd WeekChecker
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

### Run Tests

```bash
./gradlew :shared:allTests
```

Or for JVM-only tests:

```bash
./gradlew :shared:jvmTest
```

## ISO-8601 Week Calculation

The week calculation follows the ISO-8601 standard:

- Weeks start on **Monday**
- Week 1 is the week containing the **first Thursday** of the year
- Week numbers range from 1 to 52 (or 53)
- Parity: `weekNumber % 2 == 0` → Even Week

## Localization

| Key | English | French |
|---|---|---|
| even_week | Even Week | Semaine paire |
| odd_week | Odd Week | Semaine impaire |
| current_week | Current Week | Semaine actuelle |
| refresh | Refresh | Actualiser |

## Testing

Unit tests cover:

- ISO-8601 week number calculation
- Week parity (even/odd)
- Week start/end dates
- Year transitions (2024→2025, 2025→2026)
- Leap years
- Edge cases (Week 1, Week 52, Week 53)
- Full year coverage verification

## License

MIT
