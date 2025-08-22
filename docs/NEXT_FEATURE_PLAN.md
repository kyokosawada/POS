# Next Feature Work Plan: Dashboard & Analytics (QuickPOS)

## 1. Overview

The core transaction, product, and cart verticals are complete and robust. The next priority is the
implementation of a modern Dashboard & Analytics module, providing business-critical insights and
elevating owner/staff experience.

---

## 2. Success Criteria

- Owners/cashiers can immediately see:
    - Sales today/this week/this month/this year
    - Top selling products (trend, chart)
    - Revenue by payment type
    - Transaction counts and averages
- All analytics update in real time with new transactions/products.
- Dashboard is responsive and fits Material3 guidelines (cards, Vico charts, dynamic layout).
- All code built using Compose, MVVM, and flows from existing repositories.

---

## 3. Planned Steps & Files

### a. Data Access/Model

- Extend TransactionRepository or add new aggregate queries for:
    - Date interval total (filter by today/week/month)
    - Top products (aggregate CartItemEntity by productId/name, sum quantity)
    - Sales breakdowns by paymentType
    - Product low-stock alerts (query ProductRepository for thresholds)

### b. ViewModel

- Create `DashboardViewModel`:
    - Expose summary flows for each dashboard widget (sales, top product, etc.)
    - Prepare chart data (e.g., Vico data models)

### c. UI

- Create `DashboardView.kt`:
    - Summary cards (total sales, transactions, low-stock alert)
    - Vico bar/line chart for sales trend
    - LazyColumn or Row for top products
    - Ensure all UI elements follow Material3 design and are responsive.

### d. Navigation

- Wire DashboardView into main NavHost as home/start destination.
- Optional: add chart details screens for advanced drilldown.

### e. Stretch/Future

- PDF report generation/download (day/week/month summary report).
- Configurable settings for analytics interval (admin panel).
- Role-based dashboard filtering (owner/cashier).

---

## 4. Concrete File List

- `ui/dashboard/DashboardView.kt`
- `ui/dashboard/DashboardViewModel.kt`
- (Data/repo/dao): Add any aggregate queries not already in ProductRepository/TransactionRepository.
- Reference charts: `Vico` dependencies already present in lib catalog.

---

## 5. Example Queries (pseudo)

- **Total Sales**: sum of TransactionEntity.total where date in [range]
- **Top Product**: group all CartItemEntities by productId, sum quantity, join ProductEntity for
  name
- **Sales by Payment**: group TransactionEntities by paymentType, sum total
- **Stock Alerts**: query products where stockQty < LOW_STOCK_THRESHOLD

---

## 6. Tips

- Use reuse existing repo flows whenever possible to avoid data duplication.
- Keep ViewModel clean; move complex aggregations to repo/data layer.
- Design with scalable data windows in mind (today/week/month/etc).

---

**This plan provides all technical context needed for the next feature sprint for Dashboard &
Analytics in QuickPOS.**
