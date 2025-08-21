# QuickPOS Dependencies Setup Guide

This document provides step-by-step instructions for adding the comprehensive set of modern Android
dependencies to the QuickPOS project.

## 🎯 Current Status

The project currently has a minimal working setup with:

- ✅ Jetpack Compose (latest stable)
- ✅ Material3 theming
- ✅ Navigation Compose
- ✅ Lifecycle & ViewModel
- ✅ Core Android dependencies

## 📋 Dependencies Configuration Status

All modern 2025 Android dependencies are pre-configured in `gradle/libs.versions.toml` with the
latest versions:

### ✅ Already Configured in TOML

- Kotlin 2.1.10 (latest stable)
- Compose BOM 2024.12.01
- Koin 4.1.0 (latest DI framework)
- Room 2.6.1 with KSP support
- CameraX 1.4.0
- ML Kit Barcode Scanning 17.3.0
- Vico Charts 2.0.0-alpha.28
- Coil 3.0.4 (latest image loading)
- And many more...

## 🚀 Step-by-Step Addition Guide

### Phase 1: Core Architecture Dependencies

Add these to `app/build.gradle.kts` dependencies block:

```kotlin
// Koin Dependency Injection - Essential for MVVM
implementation("io.insert-koin:koin-android:4.1.0")
implementation("io.insert-koin:koin-androidx-compose:4.1.0")
implementation("io.insert-koin:koin-core:4.1.0")

// Coroutines - Essential for async operations  
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")

// Timber - Structured logging
implementation("com.jakewharton.timber:timber:5.0.1")

// LeakCanary - Memory leak detection (debug only)
debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
```

### Phase 2: Data & Serialization

```kotlin
// Room Database - Local data storage
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
// Note: Room compiler requires KSP plugin - see KSP setup below

// DataStore - Modern preferences storage
implementation("androidx.datastore:datastore-preferences:1.1.1")

// Kotlinx Serialization - JSON handling
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
```

### Phase 3: Camera & Barcode Scanning

```kotlin
// CameraX - Modern camera API
implementation("androidx.camera:camera-core:1.4.0")
implementation("androidx.camera:camera-camera2:1.4.0")
implementation("androidx.camera:camera-lifecycle:1.4.0")
implementation("androidx.camera:camera-view:1.4.0")

// ML Kit - Barcode scanning
implementation("com.google.mlkit:barcode-scanning:17.3.0")

// Permissions - Runtime permission handling
implementation("com.google.accompanist:accompanist-permissions:0.32.0")
```

### Phase 4: Charts & Visualization

```kotlin
// Vico Charts - Modern Compose-native charts
implementation("com.patrykandpatryk.vico:compose:2.0.0-alpha.28")
implementation("com.patrykandpatryk.vico:compose-m3:2.0.0-alpha.28")
implementation("com.patrykandpatryk.vico:core:2.0.0-alpha.28")
```

### Phase 5: Image Loading & UI Enhancements

```kotlin
// Coil - Modern image loading for Compose
implementation("io.coil-kt.coil3:coil-compose:3.0.4")
implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

// Material Icons Extended
implementation(libs.androidx.compose.material.icons.extended)

// System UI Controller
implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
```

### Phase 6: File Handling & Export

```kotlin
// File I/O - Modern file operations
implementation("com.squareup.okio:okio:3.9.1")

// CSV Export
implementation("com.opencsv:opencsv:5.9")

// Background Tasks
implementation("androidx.work:work-runtime-ktx:2.9.1")
```

## 🔧 Plugin Configuration

### Adding KSP Plugin (Required for Room)

1. **Add to root `build.gradle.kts`:**

```kotlin
plugins {
    // ... existing plugins
    id("com.google.devtools.ksp") version "2.1.10-1.0.29" apply false
}
```

2. **Add to app `build.gradle.kts` plugins block:**

```kotlin
plugins {
    // ... existing plugins
    id("com.google.devtools.ksp")
}
```

3. **Add Room compiler with KSP:**

```kotlin
dependencies {
    // ... other dependencies
    ksp("androidx.room:room-compiler:2.6.1")
}
```

### Adding Kotlin Serialization Plugin

1. **Add to root `build.gradle.kts`:**

```kotlin
plugins {
    // ... existing plugins
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10" apply false
}
```

2. **Add to app `build.gradle.kts` plugins block:**

```kotlin
plugins {
    // ... existing plugins
    id("org.jetbrains.kotlin.plugin.serialization")
}
```

## 🧪 Testing Dependencies

### Adding Roborazzi (Screenshot Testing)

1. **Add plugin to root `build.gradle.kts`:**

```kotlin
plugins {
    // ... existing plugins
    id("io.github.takahirom.roborazzi") version "1.29.0" apply false
}
```

2. **Add to app `build.gradle.kts`:**

```kotlin
plugins {
    // ... existing plugins
    id("io.github.takahirom.roborazzi")
}

dependencies {
    // ... existing dependencies
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.29.0")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-compose:1.29.0")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:1.29.0")
}
```

## ⚡ Performance Optimizations

### Kotlin Compiler Options

Add to `app/build.gradle.kts` android block:

```kotlin
kotlinOptions {
    jvmTarget = "17"
    freeCompilerArgs += listOf(
        "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
        "-opt-in=androidx.camera.core.ExperimentalGetImage",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
    )
}
```

### Compose Compiler Metrics (Optional)

Enable Compose compiler metrics for performance analysis:

```bash
./gradlew assembleRelease \
  -PenableComposeCompilerMetrics=true \
  -PenableComposeCompilerReports=true
```

## 🔍 Version Catalog Benefits

Using Gradle Version Catalogs provides:

- **Centralized version management** - All versions in one place
- **Type safety** - IDE autocompletion and error checking
- **Dependency bundles** - Logical grouping of related dependencies
- **Version alignment** - Automatic version conflict resolution
- **Easy updates** - Change version once, update everywhere

## 🚨 Important Notes

### Dependency Resolution Issues

If you encounter "Unresolved reference" errors:

1. **Check TOML syntax** - Ensure no syntax errors in `libs.versions.toml`
2. **Gradle sync** - Clean and rebuild project
3. **IDE restart** - Sometimes Android Studio needs a restart
4. **Manual dependency addition** - Use explicit dependency declarations as fallback

### Example Manual Addition (if version catalog fails):

```kotlin
// Instead of: implementation(libs.koin.android)
// Use: implementation("io.insert-koin:koin-android:4.1.0")
```

## 📚 Next Steps

1. **Start with Phase 1** dependencies for core architecture
2. **Implement basic features** before adding complex dependencies
3. **Add dependencies incrementally** as features require them
4. **Test each phase** to ensure stability
5. **Follow the implementation plan** in the main README.md

## 🎯 Migration Strategy

For existing projects:

1. **Backup current build files**
2. **Add dependencies in phases** (don't add all at once)
3. **Test build after each phase**
4. **Update code gradually** to use new dependencies
5. **Run tests frequently** to catch issues early

---

**Happy coding! 🚀** The modern Android development setup is now ready for the QuickPOS project.