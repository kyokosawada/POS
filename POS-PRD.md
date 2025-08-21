# Product Requirements Document (PRD): QuickPOS – Mobile Point of Sale

## 1. App Overview

**Name:** QuickPOS  
**Type:** Mobile Point of Sale (POS) for small businesses  
**Tagline:** "Checkout made simple – sales, receipts, and inventory, all in your pocket"  
**Visuals:** Cash register or scanner icon, green/blue accent, custom logo theme

---

## 2. Purpose, Goals & Vision

- **Enable fast, reliable sales tracking and checkout**
- **Automate inventory updates and error reduction**
- **Create professional, shareable receipts**
- **Deliver actionable sales insights directly on device**
- **Accessible, fast UX for any staff member**

---

## 3. User Personas

**Owner (Maria):**  
Wants to review sales, manage stock, see fast insights

**Cashier (Liam):**  
Needs fast product lookup/scan and checkout for customers

---

## 4. Key Features

### 4.1 Product Management

- Add/edit products (name, price, barcode/SKU, stock)
- Camera barcode scanning (CameraX + ML Kit)
- Photo upload (Coil Compose, optional)

### 4.2 Sales Flow

- Cart: add/remove/update products, quantity
- Fast checkout (choose payment type for demo)
- Auto stock decrement

### 4.3 Receipt Sharing

- Generate PDF receipt (Android PDF API/PdfDocument)
- Save, print, share via email/social/messaging

### 4.4 Transaction History

- List, filter, and view past sales
- PDF receipt preview/download

### 4.5 Basic Analytics

- Dashboard with today/week/month sales, top items
- Modern charts (Vico for Compose)

### 4.6 Utilities & Branding

- Export CSV of sales/inventory (kotlinx-serialization)
- Low-stock alerts
- Customizable shop branding (logo/theme)
- Dark mode toggle with Material3 dynamic theming

#### Stretch Goals

- Multi-user (owner/cashier roles)
- Hourly sales chart, inventory movement trends
- Customer info capture (for receipts)
- Cloud sync (Firebase or Ktor Client, later)

---

## 5. Data Entities

- **Product:** `{id, name, price, sku, barcode, stockQty, category, imageUrl?, created, updated}`
- **CartItem:** `{productId, name, price, quantity, subtotal}`
- **Transaction:** `{id, date, items: CartItem[], total, paymentType, receiptPdfUri}`
- **User:** `{id, name, role}`
- **Settings:** `{shopName, logoUri, themeColor, currency, taxRate}`

---

## 6. UX/UI & Visual Guidelines

- **Navigation:** Navigation Compose with bottom navigation
- **UI Framework:** Jetpack Compose with Material3 design system
- **Theming:** Dynamic Material3 theming, dark mode support
- **Interactions:** Touch-friendly with haptic feedback, animated state transitions
- **Accessibility:** High contrast support, semantic content descriptions, large touch targets
- **Architecture:** MVVM pattern with Repository and Use Cases
- **State Management:** StateFlow + Compose State integration

---

## 7. Example User Stories

- Scan product barcode, add to cart quickly
- Alert for low stock, update product easily
- Email receipt in PDF format after sale
- Export CSV for accounting
- Open dashboard and instantly see top sale stats

---

## 8. Sample Demo Data

**Products**

- Apple (SKU 10001, ₱30, stock: 100)
- Water (SKU 10002, ₱15, stock: 40)
- Sandwich (SKU 10003, ₱60, stock: 25)

**Transactions**

- Sale: [Apple ×2, Water ×1], cash, ₱75, [2024-07-11, 10:41:22]

---

## 9. Modern 2025 Android Tech Stack

### Core Framework
- **Language:** Kotlin 2.1.x (latest stable with K2 compiler)
- **Build System:** Gradle 8.x with Version Catalogs (libs.versions.toml)
- **Target SDK:** 35 (Android 15)
- **Min SDK:** 24 (Android 7.0)

### Architecture & DI
- **Architecture:** MVVM + Repository Pattern + Use Cases
- **DI:** Koin 4.0+ (Kotlin-first, lightweight DI)
- **ViewModels:** Jetpack ViewModel + Compose integration
- **State Management:** StateFlow + Compose State
- **Navigation:** Navigation Compose

### UI Framework
- **UI:** Jetpack Compose (latest stable with Compose BOM)
- **Design System:** Material3 with dynamic theming
- **System UI:** Accompanist System UI Controller (status bar, navigation bar)
- **Animations:** Compose Animation APIs (built-in)

### Data & Persistence
- **Database:** Room 2.6.x with KSP (Kotlin Symbol Processing)
- **Preferences:** Jetpack DataStore (Preferences + Proto DataStore)
- **Serialization:** kotlinx-serialization (JSON + CSV)
- **File I/O:** Okio 3.x

### Camera & Barcode Scanning
- **Camera:** CameraX 1.4.x (latest stable)
- **Barcode Scanning:** Google ML Kit Barcode Scanning 17.3.0+
- **Permissions:** Accompanist Permissions 0.32.0+

### Image & Media
- **Image Loading:** Coil 3.x (Compose-native with full Compose integration)
- **Image Selection:** Activity Result API (modern approach)

### Charts & Visualization
- **Charts:** Vico 2.x (Compose-native, multiplatform chart library)
- **Alternative:** Custom Canvas-based charts with Compose

### File Export & Sharing
- **PDF Generation:** Android PdfDocument API (native, no external dependencies)
- **File Sharing:** Android Sharesheet API
- **CSV Export:** kotlinx-serialization or OpenCSV 5.x

### Background & Async
- **Coroutines:** Kotlin Coroutines 1.8.x
- **Background Tasks:** WorkManager (for future cloud sync, report generation)

### Networking (Future Use)
- **HTTP Client:** Ktor Client 3.x (Kotlin-native) or OkHttp 4.x + Retrofit 2.x
- **JSON:** kotlinx-serialization

### Development Tools
- **Logging:** Timber 5.0.1
- **Memory Leak Detection:** LeakCanary 2.x (debug builds)
- **Build Configuration:** BuildConfig with variant-specific configurations

### Development Tools
- **Logging:** Timber 5.0.1
- **Memory Leak Detection:** LeakCanary 2.x (debug builds only)
- **Build Configuration:** BuildConfig with variant-specific configurations

---

## 10. Detailed Dependency Configuration

```kotlin
// libs.versions.toml
[versions]
kotlin = "2.1.0"
compose-bom = "2024.12.01"
koin = "4.0.0"
room = "2.6.1"
camerax = "1.4.0"
coil = "3.0.4"
vico = "2.0.0-alpha.28"
navigation-compose = "2.8.4"
datastore = "1.1.1"
kotlinx-serialization = "1.7.3"

[libraries]
# Compose
androidx-compose-bom = { module = "androidx.compose:compose-bom", version.ref = "compose-bom" }
androidx-compose-ui = { module = "androidx.compose.ui:ui" }
androidx-compose-ui-tooling = { module = "androidx.compose.ui:ui-tooling" }
androidx-compose-material3 = { module = "androidx.compose.material3:material3" }
androidx-activity-compose = { module = "androidx.activity:activity-compose", version = "1.9.2" }

# Navigation
androidx-navigation-compose = { module = "androidx.navigation:navigation-compose", version.ref = "navigation-compose" }

# Koin DI
koin-android = { module = "io.insert-koin:koin-android", version.ref = "koin" }
koin-androidx-compose = { module = "io.insert-koin:koin-androidx-compose", version.ref = "koin" }
koin-androidx-navigation = { module = "io.insert-koin:koin-androidx-navigation", version.ref = "koin" }

# ViewModel & Lifecycle
androidx-lifecycle-viewmodel-compose = { module = "androidx.lifecycle:lifecycle-viewmodel-compose", version = "2.8.7" }
androidx-lifecycle-runtime-compose = { module = "androidx.lifecycle:lifecycle-runtime-compose", version = "2.8.7" }

# Database
androidx-room-runtime = { module = "androidx.room:room-runtime", version.ref = "room" }
androidx-room-ktx = { module = "androidx.room:room-ktx", version.ref = "room" }
androidx-room-compiler = { module = "androidx.room:room-compiler", version.ref = "room" }

# DataStore
androidx-datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }

# Camera & ML Kit
androidx-camera-core = { module = "androidx.camera:camera-core", version.ref = "camerax" }
androidx-camera-camera2 = { module = "androidx.camera:camera-camera2", version.ref = "camerax" }
androidx-camera-lifecycle = { module = "androidx.camera:camera-lifecycle", version.ref = "camerax" }
androidx-camera-view = { module = "androidx.camera:camera-view", version.ref = "camerax" }
mlkit-barcode-scanning = { module = "com.google.mlkit:barcode-scanning", version = "17.3.0" }

# Charts
vico-compose = { module = "com.patrykandpatryk.vico:compose", version.ref = "vico" }
vico-compose-m3 = { module = "com.patrykandpatryk.vico:compose-m3", version.ref = "vico" }

# Image Loading
coil-compose = { module = "io.coil-kt.coil3:coil-compose", version.ref = "coil" }

# Serialization
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinx-serialization" }

# Utilities
timber = { module = "com.jakewharton.timber:timber", version = "5.0.1" }
accompanist-systemuicontroller = { module = "com.google.accompanist:accompanist-systemuicontroller", version = "0.32.0" }
accompanist-permissions = { module = "com.google.accompanist:accompanist-permissions", version = "0.32.0" }
okio = { module = "com.squareup.okio:okio", version = "3.9.1" }

# File handling
opencsv = { module = "com.opencsv:opencsv", version = "5.9" }

[plugins]
android-application = { id = "com.android.application", version = "8.7.2" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version = "2.1.0-1.0.29" }
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

---

## 11. Architecture Overview

### MVVM + Repository Pattern
- **UI Layer:** Composables + ViewModels
- **Domain Layer:** Use Cases (business logic)
- **Data Layer:** Repositories + Room Database + DataStore

### Dependency Injection with Koin
- **Modules:** Organized by feature (database, repository, viewmodel, network)
- **Scoping:** Singleton for repositories, ViewModels scoped to navigation graph
- **Testing:** Easy mocking with Koin test utilities

### State Management
- **StateFlow:** For reactive data streams
- **Compose State:** For UI state and user interactions
- **Navigation State:** Managed by Navigation Compose

---

## 12. Key Modern Practices Implementation

### 1. **Compose-First Design**
All UI built with Jetpack Compose, no XML layouts

### 2. **Material3 + Dynamic Theming**
Modern design system with system theme adaptation

### 3. **Type-Safe Navigation**
Navigation Compose with type-safe arguments

### 4. **KSP over KAPT**
Faster build times for Room and other annotation processors

### 5. **Structured Concurrency**
Proper coroutine scoping in ViewModels and Repositories

### 6. **Modern Camera API**
CameraX with ML Kit integration for barcode scanning

### 7. **Performance Optimized**
- Compose stability optimizations
- LazyColumn for large lists
- Remember for expensive computations
- Proper state hoisting

---

## 13. MVP Checklist

- ✅ Product management with Room database
- ✅ Camera barcode scanning with CameraX + ML Kit
- ✅ Cart/checkout flow with Compose UI
- ✅ PDF receipt generation with Android PDF API
- ✅ Inventory decrement/alerts with StateFlow
- ✅ Transaction history with Room queries
- ✅ CSV export with kotlinx-serialization
- ✅ Dashboard with Vico charts
- ✅ Material3 theming and dark mode
- ✅ Koin dependency injection
- ✅ Navigation Compose routing
- ✅ Offline-first architecture

---

## 14. Accessibility & Performance

### Accessibility
- **Semantic Content:** Proper content descriptions for screen readers
- **Touch Targets:** Minimum 48dp touch targets
- **Contrast:** Material3 high contrast theme support
- **Navigation:** TalkBack-friendly navigation flow

### Performance
- **Database:** Room with proper indexing and queries
- **UI:** Compose performance best practices
- **Memory:** Proper lifecycle management with ViewModels
- **Background:** WorkManager for long-running tasks

---

## 15. Code Quality & Analysis

### Development Standards
- **Static Analysis:** Detekt for Kotlin code analysis
- **Code Formatting:** ktlint for consistent code style
- **Architecture:** Clear separation of concerns with MVVM pattern
- **Documentation:** KDoc for public APIs

---

**Updated for 2025 Android development practices with Koin DI, modern Compose-native libraries, and current non-deprecated dependencies. Ready for professional development and production deployment.**

---

## Recommended MVP Feature Development Sequence (2025 Best Practice)

Following sequential thinking, industry experience, and your PRD priorities, the ideal order for
feature implementation is as follows:

1. **Product Management (Inventory)**
    - Build the product list (search/filter, add/edit/delete products, photo upload, barcode/scan if
      available).
    - *Reason:* Everything else (cart, sales, receipts) needs correct product data. This validates
      Room/database, DB<->UI sync, CRUD patterns.

2. **Cart & Checkout Flow**
    - Add/remove/update cart items, quantity adjustment, perform checkout, select payment type.
    - *Reason:* Core to daily POS usage. Exercises product data in “live” transactional context,
      tests state updates, and edge UX cases.

3. **Transaction History & Receipts**
    - List/filter past sales, show receipt/PDF, share/email/download actions.
    - *Reason:* Leverages both product and sales data. Implements historical persistence and
      archiving flows—verifies your end-to-end pipeline is robust.

4. **Dashboard/Analytics**
    - Show daily/weekly/monthly sales, top items, modern charts (Vico for Compose, etc).
    - *Reason:* Needs real transaction data. Surfaces business value and actionable insights for
      owners and staff.

5. **Settings & Branding**
    - Shop branding (logo/theme), CSV export, low-stock alerts, currency/tax settings, dark mode,
      preferences management.
    - *Reason:* Enhances user experience and branding. Typically decoupled from MVP flows and can be
      developed in parallel or after main flows.

6. **Stretch/Advanced**
    - Multi-user/roles (owner/cashier), advanced analytics, customer info capture, cloud sync, and
      any new priorities.
    - *Reason:* Defer until MVP is stable and tested; these build upon core foundation and add
      business differentiation.

**Approach:**

- Build feature-by-feature: Implement only the data models, repositories, ViewModels, and UI needed
  for each slice.
- Test each vertical slice in isolation (UI ↔️ ViewModel ↔️ Repository ↔️ DB) for rapid feedback and
  better quality.
- This iterative vertical-slice development keeps code clean, feedback loops tight, and features
  demonstrable after each step.

---