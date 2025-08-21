# QuickPOS - Modern Android Point of Sale System

## Overview

QuickPOS is a modern mobile Point of Sale (POS) system built with the latest 2025 Android
development practices. This project showcases modern Android architecture using Jetpack Compose,
Koin DI, Room database, and other cutting-edge technologies.

## 🚀 Modern 2025 Android Tech Stack

### Core Framework

- **Language:** Kotlin 2.1.10 (latest stable with K2 compiler)
- **Build System:** Gradle 8.7.2 with Version Catalogs (libs.versions.toml)
- **Target SDK:** 35 (Android 15)
- **Min SDK:** 24 (Android 7.0)
- **Java Target:** 17 (Latest LTS)

### Architecture & DI

- **Architecture:** MVVM + Repository Pattern + Use Cases
- **DI:** Koin 4.1.0 (Kotlin-first, lightweight DI)
- **ViewModels:** Jetpack ViewModel + Compose integration
- **State Management:** StateFlow + Compose State
- **Navigation:** Navigation Compose 2.8.4

### UI Framework

- **UI:** Jetpack Compose (latest stable with Compose BOM 2024.12.01)
- **Design System:** Material3 with dynamic theming
- **System UI:** Accompanist System UI Controller
- **Animations:** Compose Animation APIs (built-in)

### Data & Persistence

- **Database:** Room 2.6.1 with KSP (Kotlin Symbol Processing)
- **Preferences:** Jetpack DataStore (Preferences)
- **Serialization:** kotlinx-serialization 1.8.0 (JSON + CSV)
- **File I/O:** Okio 3.9.1

### Camera & Barcode Scanning

- **Camera:** CameraX 1.4.0 (latest stable)
- **Barcode Scanning:** Google ML Kit Barcode Scanning 17.3.0+
- **Permissions:** Accompanist Permissions 0.32.0

### Image & Media

- **Image Loading:** Coil 3.0.4 (Compose-native with full Compose integration)
- **Image Selection:** Activity Result API (modern approach)

### Charts & Visualization

- **Charts:** Vico 2.0.0-alpha.28 (Compose-native, multiplatform chart library)

### File Export & Sharing

- **PDF Generation:** Android PdfDocument API (native, no external dependencies)
- **File Sharing:** Android Sharesheet API
- **CSV Export:** kotlinx-serialization + OpenCSV 5.9

### Background & Async

- **Coroutines:** Kotlin Coroutines 1.10.1
- **Background Tasks:** WorkManager 2.9.1 (for future cloud sync, report generation)

### Development Tools

- **Logging:** Timber 5.0.1
- **Memory Leak Detection:** LeakCanary 2.14 (debug builds)
- **Build Configuration:** BuildConfig with variant-specific configurations

## 🛠️ Project Setup

### Prerequisites

- Android Studio Ladybug | 2024.2.1 or newer
- JDK 17 or newer
- Android SDK 35
- Gradle 8.7.2

### Getting Started

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd quickpos
   ```

2. **Open in Android Studio:**
    - Open Android Studio
    - Select "Open an existing project"
    - Navigate to the project directory
    - Wait for Gradle sync to complete

3. **Build the project:**
   ```bash
   ./gradlew clean assembleDebug
   ```

## 📦 Dependencies Configuration

The project uses Gradle Version Catalogs for dependency management. All dependencies are defined in
`gradle/libs.versions.toml` and organized by feature:

### Current Essential Dependencies

```kotlin
// Core Android & Compose
implementation(libs.androidx.core.ktx)
implementation(libs.androidx.activity.compose)
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.material3)

// Navigation & Lifecycle
implementation(libs.androidx.navigation.compose)
implementation(libs.androidx.lifecycle.viewmodel.compose)

// Koin DI
implementation(libs.koin.android)
implementation(libs.koin.androidx.compose)

// Room Database
implementation(libs.androidx.room.runtime)
implementation(libs.androidx.room.ktx)

// Coroutines & Serialization
implementation(libs.kotlinx.coroutines.android)
implementation(libs.kotlinx.serialization.json)

// Development Tools
implementation(libs.timber)
debugImplementation(libs.leakcanary.android)
```

### Additional Dependencies to Add

The following dependencies are configured in `libs.versions.toml` and ready to be added to
`app/build.gradle.kts` as features are implemented:

#### Camera & Barcode Scanning

```kotlin
implementation(libs.androidx.camera.core)
implementation(libs.androidx.camera.camera2)
implementation(libs.androidx.camera.lifecycle)
implementation(libs.androidx.camera.view)
implementation(libs.mlkit.barcode.scanning)
implementation(libs.accompanist.permissions)
```

#### Charts (Vico)

```kotlin
implementation(libs.vico.compose)
implementation(libs.vico.compose.m3)
implementation(libs.vico.core)
```

#### Image Loading

```kotlin
implementation(libs.coil.compose)
implementation(libs.coil.network.okhttp)
```

#### File Export

```kotlin
implementation(libs.okio)
implementation(libs.opencsv)
```

## 🏗️ Architecture Overview

### MVVM + Repository Pattern

```
UI Layer (Composables) 
    ↓
ViewModels (State Management)
    ↓
Use Cases (Business Logic)
    ↓
Repositories (Data Access)
    ↓
Data Sources (Room, Network, DataStore)
```

### Dependency Injection with Koin

- **Modules:** Organized by feature (database, repository, viewmodel, network)
- **Scoping:** Singleton for repositories, ViewModels scoped to navigation graph
- **Testing:** Easy mocking with Koin test utilities

### State Management

- **StateFlow:** For reactive data streams from ViewModels
- **Compose State:** For UI state and user interactions
- **Navigation State:** Managed by Navigation Compose

## 🎯 Key Features Implementation Plan

### Phase 1: Core Foundation ✅

- [x] Modern Android project setup
- [x] Jetpack Compose UI framework
- [x] Koin dependency injection
- [x] Room database setup
- [x] Navigation Compose
- [x] Material3 theming

### Phase 2: Business Logic

- [ ] Product management (CRUD operations)
- [ ] Shopping cart functionality
- [ ] Transaction processing
- [ ] Receipt generation (PDF)
- [ ] Basic analytics dashboard

### Phase 3: Advanced Features

- [ ] Camera barcode scanning
- [ ] Image upload for products
- [ ] CSV export functionality
- [ ] Advanced charts with Vico
- [ ] Background sync with WorkManager

### Phase 4: Polish & Optimization

- [ ] Performance optimization
- [ ] Accessibility improvements
- [ ] Dark mode support
- [ ] Localization
- [ ] Testing coverage

## 🧪 Testing Strategy

### Unit Testing

- JUnit 4 for business logic
- Mockito/MockK for mocking
- Koin testing utilities

### UI Testing

- Compose testing framework
- Roborazzi for screenshot testing (planned)
- Espresso for integration tests

### Development Testing

- LeakCanary for memory leak detection
- Timber for structured logging
- Debug builds with additional tools

## 📱 Build Variants

### Debug

- Application ID: `com.quickpos.debug`
- Debugging enabled
- LeakCanary included
- Additional logging

### Release

- Application ID: `com.quickpos`
- Code minification and shrinking enabled
- ProGuard optimization
- Production logging level

## 🚀 Development Best Practices

### Code Quality

- Kotlin coding standards
- Compose performance best practices
- Proper lifecycle management
- Memory leak prevention

### Modern Android Practices

- Single Activity architecture
- Compose-first UI development
- Structured concurrency with coroutines
- Type-safe navigation
- Reactive programming with StateFlow

### Performance

- Lazy loading for large lists
- Proper state hoisting
- Efficient recomposition
- Image loading optimization

## 📚 Key Dependencies Documentation

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Koin Dependency Injection](https://insert-koin.io/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
- [Vico Charts](https://patrykandpatrick.com/vico/api/stable/)
- [CameraX](https://developer.android.com/jetpack/androidx/releases/camera)
- [ML Kit Barcode Scanning](https://developers.google.com/ml-kit/vision/barcode-scanning)

## 🤝 Contributing

1. Follow the established architecture patterns
2. Write tests for new features
3. Use the configured linting and formatting rules
4. Update documentation for significant changes
5. Follow Material Design guidelines for UI

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.