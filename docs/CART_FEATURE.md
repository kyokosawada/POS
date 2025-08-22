# Cart Feature Documentation

This document provides a detailed overview of the Cart feature in the QuickPOS application, covering
its architecture, components, user flow, and implementation details.

---

## 1. Overview

The Cart feature provides a dedicated screen for managing items a customer intends to purchase. It
allows for adding products from the inventory, updating item quantities, removing items, and viewing
a running total. The feature is built with a modern Android architecture, ensuring it is scalable,
testable, and maintainable.

### Core Functionality:

- **Add to Cart**: Users can add products to the cart via a dialog that lists available inventory.
- **Update Quantity**: Users can increment or decrement the quantity of items directly in the cart.
- **Remove Item**: Users can remove individual items from the cart.
- **Clear Cart**: A dedicated button allows for clearing all items from the cart at once.
- **Live Total**: The total price is calculated and displayed in real-time as the cart is modified.
- **Stock Validation**: The UI prevents adding products that are out of stock and provides visual
  feedback.
- **Checkout Trigger**: A checkout button initiates the next step in the sales flow (currently a
  placeholder action).
- **Responsive UI**: The layout adapts to different screen sizes using `WindowSizeClass`.

---

## 2. Architecture & Layers

The Cart feature strictly follows the **MVVM (Model-View-ViewModel)** pattern combined with
principles from **Clean Architecture**, such as the use of a Repository and Use Cases.

- **UI Layer (`CartView.kt`)**: A Jetpack Compose screen that is completely stateless. It observes
  state from the `CartViewModel` and delegates all user actions to it. It contains the main
  `CartView` composable and private helper composables like `CartItemRow` and `AddProductDialog`.

- **ViewModel (`CartViewModel.kt`)**: The state holder and logic controller for the UI. It receives
  user events, calls the appropriate **Use Cases**, and exposes the cart's state as a `StateFlow`
  for the UI to collect.

- **Domain Layer (Use Cases)**: Each specific business action is encapsulated in its own Use Case
  class (e.g., `AddToCartUseCase`, `RemoveFromCartUseCase`). These classes are lightweight and call
  methods on the `CartRepository`. This isolates business logic and makes it highly testable.

- **Data Layer (`CartRepository.kt`, `CartRepositoryImpl.kt`)**: This layer is responsible for
  managing the cart data itself.
    - The `CartRepository` interface defines the contract for cart operations.
    - The `CartRepositoryImpl` is a concrete implementation that currently manages the cart state *
      *in-memory** using a `MutableStateFlow`. This can be easily swapped for a persistent storage
      solution (like Room) in the future without changing the ViewModel or UI.

- **Dependency Injection (Koin)**: All dependencies (`ViewModel`, `UseCases`, `Repository`) are
  provided through Koin. This decouples the layers and simplifies testing.
    - `CartRepository` is provided as a `single` to ensure a single, shared cart state across the
      entire app.
    - `UseCases` and `ViewModels` are provided as `factory` to ensure new instances are created as
      needed.

### Diagram: Data Flow

```
[ UI (CartView) ]  -->  [ CartViewModel ]  -->  [ Use Cases ]  -->  [ CartRepository ]
      ^                                                                      |
      |___________________________(observes StateFlow)________________________|

```

---

## 3. User Flow: Adding a Product

1. **User Taps FAB**: The user taps the Floating Action Button (`+`) on the `CartView` screen.
2. **Dialog Appears**: An `AlertDialog` (`AddProductDialog`) is displayed, showing a list of
   available products fetched via the `ProductViewModel`.
3. **Stock Check (UI)**: Products with zero stock (`stockQty <= 0`) are visually disabled and marked
   as "Out of stock". They are not clickable.
4. **User Selects Product**: The user taps on an in-stock product from the list.
5. **Action Delegated**: The `onAdd` lambda is triggered, which calls `viewModel.addItem()`.
6. **Stock Check (Logic)**: An additional check `if (product.stockQty <= 0)` is performed. If the
   product is out of stock, a `Snackbar` is shown. Otherwise, a `CartItem` is created.
7. **ViewModel Calls Use Case**: The `CartViewModel` calls `addToCart(item)` (which is an instance
   of `AddToCartUseCase`).
8. **Repository Updates State**: The `AddToCartUseCase` calls
   `cartRepository.addOrUpdateItem(item)`. The `CartRepositoryImpl` updates its internal list and
   emits the new state through its `StateFlow`.
9. **UI Updates**: The `CartView` collects the new state from the `StateFlow` and automatically
   recomposes to display the newly added item in the `LazyColumn`.

---

## 4. Key Implementation Details

- **State Management**: The primary state (`cartItems`) is held in the `CartRepositoryImpl` as a
  `MutableStateFlow`. The ViewModel exposes this as an immutable `StateFlow` to the UI, following
  best practices for unidirectional data flow.

- **Responsive Paddings**: Instead of hardcoded `dp` values, the UI uses an adaptive padding system
  based on `WindowSizeClass`. The `getAdaptiveValues()` utility provides `horizontalPadding` values
  that adjust for mobile, tablet, and landscape layouts.

- **Centered TopAppBar**: The `TopAppBar` title is centered using a `Box` with
  `Modifier.fillMaxWidth()` and `contentAlignment = Alignment.Center` to ensure a consistent look
  and feel with other screens.

- **Button Visibility and State**: The `Clear` button is always visible but is only `enabled` when
  `cartItems.isNotEmpty()`, providing clear and consistent UX.

---

## 5. Future Enhancements

- **Persistent Cart**: The `CartRepositoryImpl` can be updated to use Room or DataStore to save the
  user's cart between app sessions.
- **Quantity Picker in Dialog**: The `AddProductDialog` could include a quantity stepper to allow
  adding more than one item at a time.
- **Advanced Stock Logic**: The `UpdateCartItemQuantityUseCase` could be enhanced to check against
  available stock when a user increases the quantity of an item already in the cart.
- **Swipe-to-Delete**: The `CartItemRow` could be wrapped in a `SwipeToDismiss` composable for a
  faster removal UX.
