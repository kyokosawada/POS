# Products List & Inventory Feature Documentation (QuickPOS)

## 1. Overview

The **Product List**/Inventory module forms the backbone of QuickPOS: all cart, sales, and analytics
flows depend on accurate and real-time product data.

**Key Objectives:**

- Provide a reliable, reactive, and extensible inventory foundation (list, search, CRUD, stock).
- Support seamless Compose/MVVM usage, including flows, live update, and easy testability.
- Reinforce best practices for non-overlapping architectures: inventory, sales, and cart remain
  cleanly separated but coherent.

---

## 2. Product Entity (`ProductEntity.kt`)

Kotlin data class, Room-annotated Entity:

```
@PrimaryKey(autoGenerate = true) val id: Long
val name: String
val price: Double
val sku: String
val barcode: String
val stockQty: Int
val category: String = ""
val imageUrl: String? = null
val created: Long = System.currentTimeMillis()
val updated: Long = System.currentTimeMillis()
```

- Always reference `stockQty` for live inventory; do not persist negative value.
- `created` and `updated` system fields for audit/tracking.

---

## 3. ProductDao (`ProductDao.kt`)

Annotated Room DAO with full CRUD, plus Flow for reactive UI/data sync:

- `fun getProducts(): Flow<List<ProductEntity>>` – sort by name ASC
- `suspend fun insertProduct(product: ProductEntity): Long`
- `suspend fun updateProduct(product: ProductEntity)`
- `suspend fun deleteProduct(product: ProductEntity)`
- `fun searchProducts(query: String): Flow<List<ProductEntity>>` – by name or barcode
- `suspend fun getProductById(id: Long): ProductEntity?`

---

## 4. ProductRepository (MVVM Contract)

### Interface

Defines the API for all product inventory ops:

- `getProducts()` – live list, for UI/analytics
- `searchProducts(query)` – advanced filtering
- `insertProduct(product)`
- `updateProduct(product)`
- `deleteProduct(product)`
- `getProductById(id)`

### Implementation

`ProductRepositoryImpl` passes calls directly to DAO (compose friendly, testable, MVVM-ready).

---

## 5. Integration Points

- **Cart Feature:** Product inventories are shown as the source for adding to cart. `stockQty` is
  referenced to limit adds/updates.
- **Transaction Feature:** On confirmed sales, ProductRepository is used to decrement `stockQty` for
  each product sold.
- **Analytics:** Data from ProductRepository feeds into charts, top sellers, low stock, etc.
- **Search:** Live search via the repository for rapid barcode/name lookup.

---

## 6. Extensibility & Best Practices

- The repository/DAO interface is stable for future upgrades (add fields, Room automigrations,
  image/media, wholesale pricing, etc).
- Always depend on Flow for UI lists; use suspending functions for single operations.
- Only the repository (never the UI) should call DAO methods, for maintainability.

---

## 7. Developer API Table

| API | Description |
|-----|-------------|
| getProducts() | Returns Flow<List<ProductEntity>>. Use for UI, analytics, selection, etc. |
| insertProduct(product) | Add a new/edited product; emits to list automatically. |
| updateProduct(product) | Edits any product field (including stockQty after sales) |
| deleteProduct(product) | Removes from DB, updates all results via Flow |
| getProductById(id) | Safe lookup for cart/transaction logic/tests. |

---

## 8. User-Visible/UX Flows

- Users/viewers always see live inventory counts; all sales and cart views sync from this source.
- Cart and Transaction modules trust this as the “final” state for limits and validation.

---

*Product List module: Designed for correctness, developer velocity, and full MVVM/reactive Compose
integration for QuickPOS and future features.*
