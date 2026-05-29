# Lightning Browser

An open-source Android web browser focused on speed, simplicity, and security. Built with Kotlin, Jetpack Compose, and Android WebView.

## Project Structure

- `app/` — Main Android application module
  - `src/main/java/acr/browser/lightning/` — Core Kotlin/Java source code
  - `src/main/res/` — Android XML layouts, drawables, strings
  - `src/main/assets/` — Ad-block hosts file, search engine icons
  - `src/main/html/` — Embedded HTML pages (bookmarks, homepage)
  - `src/main/js/` — Injected JavaScript (InvertPage, TextReflow, ThemeColor)
- `gradle/` — Gradle wrapper configuration

## Build System

- **Language**: Kotlin + Java
- **Build tool**: Gradle 9.5.1 (Kotlin DSL)
- **Min SDK**: 26 | **Target SDK**: 36
- **JDK required**: 21 (project uses `kotlin { jvmToolchain(21) }`)
- **Product flavors**: `lightningPlus` (full version) and `lightningLite`

## Building

Use the **Build** workflow or run from the Shell:

```bash
./gradlew assembleLightningPlusDebug
```

> **Note**: This is a native Android app. It cannot run as a web server in the Replit preview pane. An Android device/emulator or CI pipeline is needed to run the APK. The Replit environment provides Java 19 (GraalVM 22.3); the project targets JDK 21, so builds may require an external CI environment (GitHub Actions is already configured in `.github/`).

## Key Dependencies

- Jetpack Compose + Material 3
- Dagger (dependency injection)
- Kotlin Coroutines + DataStore
- OkHttp / Okio
- Coil (image loading)
- JSoup (HTML parsing)
- Mezzanine (file embedding at compile time)

## Developer Contact

- **Developer:** Riaz Ali Shahani
- **Email:** riazalishahani485@gmail.com
- **WhatsApp:** +923301458939

## User Preferences

<!-- Add any user preferences or conventions here -->
