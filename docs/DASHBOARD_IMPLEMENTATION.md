# Dashboard & Analytics Implementation

## Overview

The Dashboard & Analytics feature has been successfully implemented for the QuickPOS system,
providing real-time business insights and sales analytics. This implementation follows the MVVM
architecture pattern and integrates seamlessly with the existing cart, product, and transaction
features.

## Features Implemented

### 1. Real-time Analytics Dashboard

- **Summary Cards**: Display key metrics including total sales, transaction count, average sale
  value, and low stock alerts
- **Date Range Filtering**: Filter analytics by Today, This Week, This Month, and This Year
- **Top Products Analysis**: Shows best-selling products by quantity and revenue
- **Payment Method Breakdown**: Analyzes sales by payment type with transaction counts
- **Low Stock Alerts**: Automatically alerts when products fall below stock threshold
- **Vico Chart Showcase** (NEW):
    - **Line Chart with Vico**: A modern sales trend line chart powered by Vico, visualizing
      daily/period totals for the selected date range.
    - **Robust Data Sync**: The chart updates live as the sales data changes, with full
      StateFlow-Compose integration.
    - **UX and Limitations**: The chart is shown for 'Week', 'Month', and 'Year' only.
    - **Today Tab Handling**: For usability and technical reasons, the line chart is hidden when '
      Today' is selected, to avoid rendering a one-point or zero-point chart. This prevents
      confusion and runtime crashes, and ensures only informative chart visualizations appear in the
      UI.
    - **Best Practice**: All axis label/series mapping is fully robust to dynamic data and UI tab
      switches.

### 2. Data Layer Extensions

#### New Data Models (`DashboardModels.kt`)

- `DashboardSummary`: Aggregated dashboard metrics
- `DailySales`: Time-series sales data for trends
- `DateRange`: Enum for filtering periods with helper functions

#### Extended Database Queries

**TransactionDao.kt** - Added aggregate queries:

- `getSalesSummary()`: Total sales, transaction count, and averages
- `getSalesByPaymentType()`: Payment method breakdown
- `getTopProducts()`: Best-selling products analysis
- `getDailySales()`: Daily sales data for trend analysis
- Real-time Flow-based queries for reactive updates

**ProductDao.kt** - Added inventory queries:

- `getLowStockCount()`: Count of products below threshold
- `getLowStockProducts()`: List of low-stock products
- Flow-based queries for real-time stock monitoring

#### Repository Extensions

- Extended `TransactionRepository` and `ProductRepository` interfaces
- Implemented new methods in `TransactionRepositoryImpl` and `ProductRepositoryImpl`
- All queries support both suspend functions and Flow-based reactive streams

### 3. ViewModel Layer

#### DashboardViewModel

- **Real-time Data Streams**: Uses StateFlow for reactive UI updates
- **Date Range Management**: Handles filtering with automatic data refresh
- **Data Formatting**: Currency formatting and date display utilities
- **Error Handling**: Graceful fallbacks for data loading failures
- **Performance Optimized**: Uses `SharingStarted.WhileSubscribed(5000)` for efficient resource
  management

Key Features:

```kotlin
class DashboardViewModel(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    
    val dashboardSummary: StateFlow<DashboardSummary>
    val salesByPaymentType: StateFlow<List<SalesByPaymentType>>
    val topProducts: StateFlow<List<TopProduct>>
    val dailySales: StateFlow<List<DailySalesResult>>
    val lowStockProducts: StateFlow<List<ProductEntity>>
    
    fun selectDateRange(dateRange: DateRange)
    fun formatCurrency(amount: Double): String
    fun formatDate(timestamp: Long): String
}
```

### 4. UI Layer

#### DashboardView (Compose)

- **Material3 Design**: Uses latest Material Design components and theming
- **Responsive Layout**: Adapts to different screen sizes with LazyColumn and LazyRow
- **Interactive Components**: FilterChips for date range selection
- **Rich Data Visualization**: Cards, lists, and summary displays
- **Real-time Updates**: Automatically refreshes when data changes
- **Error States**: Graceful handling of empty data states
- **Vico Chart Logic**:
    - The line chart is only rendered if a valid multi-point trend exists. 'Today' skips chart
      drawing by design (see above).
    - All x-axis labels and coordinate access are mathematically safe, aligned with modern Compose
      code standards.

Key Components:
- `DashboardHeader`: Date range selector with Material3 FilterChips
- `SummaryCards`: Key metrics in colored cards with icons
- `TopProductsList`: Best-selling products with sales data
- `PaymentTypeBreakdown`: Payment method analysis
- `LowStockAlerts`: Prominent inventory warnings
- `SalesTrendLineChart` (Vico): See above for details

### 5. Dependency Injection

Updated `DataModule.kt` to include:

```kotlin
// Dashboard features
factory { DashboardViewModel(get(), get()) } // TransactionRepository, ProductRepository
```

## Technical Implementation Details

### Database Schema Extensions

#### New Query Results

```kotlin
data class SalesSummaryResult(
    val totalSales: Double,
    val totalTransactions: Int,
    val averageTransactionValue: Double
)

data class DailySalesResult(
    val dateString: String,
    val date: Long,
    val sales: Double,
    val transactionCount: Int
)
```

#### Complex SQL Queries

- **Sales Summary**: Aggregates transaction totals with COALESCE for null safety
- **Top Products**: JOINs cart_items and transactions, groups by product
- **Payment Analysis**: Groups transactions by payment type
- **Daily Sales**: Uses SQLite date functions for time-based grouping
- **Low Stock**: Simple threshold-based inventory queries

### Real-time Data Flow Architecture

```
Database (Room) → Repository (Flow) → ViewModel (StateFlow) → UI (Compose)
```

- **Room**: Provides Flow-based queries for reactive database updates
- **Repository**: Exposes Flow streams that emit on data changes
- **ViewModel**: Combines multiple streams and manages UI state
- **Compose**: Collects StateFlow with `collectAsStateWithLifecycle()`

### Vico Chart Integration: Details & Rationale

- **Library**: [Vico Compose](https://github.com/patrykandpatrick/vico)
- **Chart Type**: Line chart for time-based business analytics
- **Chart Display**:
    - Shown for 'Week', 'Month', and 'Year' with daily aggregated data as trend line
    - Hidden for 'Today' to prevent single-point trend, UX confusion, or out-of-bounds crashes
    - All axis value mapping and state updates are defensive and safe for all dataset changes/tab
      switches
- **Custom Axis Label Formatter**: Axis label code is robust to odd tick values or empty data, never
  crashing the UI
- **Future Ready**: Can swap in bar/area chart, hourly data, or multi-series trends as needed

### Performance Optimizations

1. **Efficient State Management**: Uses `SharingStarted.WhileSubscribed(5000)` to stop collecting
   when UI is not active
2. **Lazy Loading**: LazyColumn and LazyRow for efficient scrolling
3. **Minimal Recomposition**: Well-structured state to prevent unnecessary recompositions
4. **Database Indexing**: Proper indexes on date and productId columns for fast queries

## Navigation Integration

The dashboard is already integrated as the home destination in `AppNavHost.kt`:

- Route: `"dashboard"`
- Icon: `Icons.Filled.Home`
- Label: "Home"
- Default start destination for the app

## Future Enhancements (Phase 2)

### Chart Integration Ready

The foundation is prepared for adding Vico charts:

- Data models already provide chart-ready data structures
- Date range filtering works with time-series data
- Chart producers can be easily added to ViewModel

### Planned Additions

1. **Sales Trend Charts**: Line charts showing daily/weekly/monthly trends
2. **Product Performance Charts**: Bar charts for top products
3. **Payment Distribution**: Pie charts for payment method breakdown
4. **PDF Report Generation**: Export dashboard data as PDF
5. **Advanced Filtering**: Custom date ranges and product categories

## Testing Considerations

The implementation is designed for easy testing:

- **Repository Layer**: Can be mocked for unit tests
- **ViewModel**: Pure business logic with testable flows
- **UI Components**: Composable functions with clear parameters
- **Data Models**: Simple data classes easy to create test data

## Build Verification

✅ **Compilation Status**: Successfully compiles with `./gradlew compileDebugKotlin`
✅ **Lint Status**: No linting errors in dashboard implementation
✅ **Architecture**: Follows established MVVM pattern
✅ **Integration**: Properly integrated with existing DI and navigation

## Code Organization

```
app/src/main/java/com/kyokosawada/
├── data/
│   ├── transaction/
│   │   ├── DashboardModels.kt          # Dashboard data models
│   │   ├── TransactionDao.kt           # Extended with analytics queries
│   │   ├── TransactionRepository.kt    # Extended interface
│   │   └── TransactionRepositoryImpl.kt # Implementation
│   ├── product/
│   │   ├── ProductDao.kt               # Extended with stock queries
│   │   ├── ProductRepository.kt        # Extended interface
│   │   └── ProductRepositoryImpl.kt    # Implementation
│   └── DataModule.kt                   # Updated DI configuration
└── ui/
    └── dashboard/
        ├── DashboardView.kt            # Main UI composable
        └── DashboardViewModel.kt       # Business logic and state
```

## Summary

The Dashboard & Analytics feature provides a comprehensive, real-time view of business performance
with:

- **4 Summary Metrics**: Sales, transactions, averages, and inventory alerts
- **5 Analytics Views**: Date filtering, top products, payment breakdown, stock alerts
- **Vico Chart Showcase**: Modern charting, safe for dynamic analytics, hidden for tabs where no
  trend is meaningful
- **Real-time Updates**: Automatic refresh when new transactions occur
- **Material3 Design**: Modern, accessible UI following design guidelines
- **Production Ready**: Error handling, performance optimizations, and proper architecture

The implementation successfully meets all requirements from the `NEXT_FEATURE_PLAN.md` and provides
a solid foundation for future chart and reporting enhancements.