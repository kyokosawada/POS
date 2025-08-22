# QuickPOS Transaction & Receipt Feature Documentation

## Core Feature Overview

This document covers the full vertical for transaction capture, checkout, and receipt/historical
record in QuickPOS as of 2025 (Kotlin, Compose, MVVM, Room).

---

## Feature Breakdown

### 1. Checkout & Transaction Execution

- Users may add items to the cart (no more than available inventory).
- At checkout:
    - User **must select a payment type** (`cash`, `credit`, `GCash`, etc.).
    - On confirmation, the system **re-checks up-to-date product stocks** for all items in the cart.
    - If all items have sufficient stock, transaction is saved as historical sale, and all related
      products’ stockQty are decremented accordingly in the database.
    - If any item now exceeds available stock, checkout is aborted. The user is shown a specific
      error (snackbar: “Not enough stock for [item]”). The cart is NOT cleared/confirmed, allowing
      for correction and retry.

### 2. Inventory Sync & Data Integrity

- **UI Enforcement**: Cart UI disables the “+” add-item action when quantity matches in-stock,
  always referencing live inventory.
- **Backend Enforcement**: Even if UI was stale or a race occurred, final checkout code validates
  all stock, preventing oversells across devices/sessions/races.
- **Decrement Logic**: Every successful transaction correctly reduces all related stocks without
  duplication or negative inventory.

### 3. Receipt & Transaction History

- Every confirmed sale is recorded with: timestamp, full line-item breakdown, payment type, and
  totals.
- Transaction history is accessible/filtered; tapping an entry shows an itemized, modern receipt
  view in-app.
- Future: Receipt export/sharing via PDF is supported by design (storage/preview fields reserved;
  full PDF export planned next phase).

### 4. Failures, Edge Cases & Error Handling

- Cart and all transaction flows gracefully alert user if any line becomes invalid due to concurrent
  actions.
- API/DB/memory exceptions are surfaced with actionable feedback—e.g. if a sale fails to commit, all
  inventory and transaction DB are rolled back with no partial decrement.
- Oversell is strictly impossible (users cannot cheat/accidentally bypass transaction logic).

---

## User Stories

- **Checkout from Cart**: “Liam” scans products for a customer, tries to purchase more than
  remaining inventory. The system blocks the extra quantities at UI and then, if necessary, at
  checkout, always explaining why and never selling more than you have.
- **Inventory Manager**: “Maria” reviews transaction history. Every receipt shows true units sold,
  payment type, and matches backend inventory state. She can trust inventory reporting at any time.

---

## Extensibility & Future Roadmap

- All data flows are MVVM-ready and testable for:
    - Refund/reversal flows (increment stock, void transaction record)
    - Multi-user sessions (future owner/cashier roles)
    - PDF/email/export for all receipts
    - Analytics (top items, revenue per payment type, etc.)
- All validation is independently testable through ViewModel and repo layers.

---

## Developer/QA Guidance

- **Important**: Any UX flow that involves mutating cart or products **must** use the official
  repo/ViewModel APIs to preserve all invariants.
- **Edge cases to test:**
    - Simultaneous sales across devices for the same product.
    - Product edited (or deleted) while in-cart.
    - Attempted oversell in both UI and final checkout.
    - App resumes after network/DB failure and reverts/commits only valid state.

---

## Open Questions / TODOs (2025+)

- PDF generation/export and advanced receipt flows.
- Refunds, void, full/partial returns (future PRD sections).
- Multi-channel sync/cloud architecture.

---

*QuickPOS Transaction logic: Designed for simplicity, trust, and ongoing extensibility for any
modern retail business.*
