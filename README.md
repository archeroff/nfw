# Week Checker

A Kotlin Multiplatform application that determines whether the current calendar week is **Even** or **Odd**, following the ISO-8601 standard. Ships as an Android app, an iOS app, and a **web app / PWA** (Kotlin/Wasm + Compose Multiplatform) that deploys to any free static host.

## Features

- Displays current ISO-8601 week number
- Shows Even/Odd week status
- Shows current date and week range (Monday - Sunday)
- Material 3 theming with light/dark mode support
- Dynamic colors on Android (Material You)
- English and French localization
- Fully offline, no network required
- Clean Architecture with MVVM pattern
- Web: installable PWA with offline support (service worker + manifest)

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
│   │   ├── iosMain/               # iOS-specific (Koin init, entry point)
│   │   └── wasmJsMain/            # Web-specific (notifications via Web Notifications API)
│   └── build.gradle.kts
├── androidApp/                    # Android application
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/weekchecker/
│   │   │   ├── MainActivity.kt
│   │   │   └── WeekCheckerApp.kt
│   │   └── res/
│   └── build.gradle.kts
├── webApp/                        # Web / PWA application (Kotlin/Wasm)
│   └── src/wasmJsMain/
│       ├── kotlin/com/weekchecker/web/App.kt
│       └── resources/             # index.html, styles.css, manifest, sw.js, icons
├── iosApp/                        # iOS SwiftUI wrapper
│   └── WeekChecker/
│       └── WeekCheckerApp.swift
├── scripts/assemble-web.sh        # Assembles the deployable static site
├── .github/workflows/deploy-pages.yml  # GitHub Pages CI/CD
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
- **Web**: JDK 17 (Node.js is used by the wasm bundle build)

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

### Web / PWA

Build the wasm distribution:

```bash
./gradlew :webApp:wasmJsBrowserDistribution
```

Run a local dev server:

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Assemble the deployable static site (the PWA) into `build/web/`:

```bash
./scripts/assemble-web.sh
```

The entire `build/web/` directory is a self-contained static site (HTML, JS, wasm,
manifest, service worker, icons) that can be hosted on GitHub Pages, Cloudflare Pages,
Vercel, or Netlify with no server required.

### Deploy to GitHub Pages

The repo ships a CI/CD workflow (`.github/workflows/deploy-pages.yml`) that builds the
wasm site and publishes it to GitHub Pages on every push to `main`:

1. In GitHub: Settings → Pages → Source → **GitHub Actions**
2. Push to `main` (or run the *Deploy web app to GitHub Pages* workflow manually)

For Cloudflare Pages / Netlify / Vercel, point the build command at
`./gradlew :webApp:wasmJsBrowserDistribution && ./scripts/assemble-web.sh` and the
publish/output directory at `build/web`.

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
