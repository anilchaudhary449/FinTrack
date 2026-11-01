# FinTrack - Architecture Documentation

## 1. Architectural Philosophy
FinTrack is built adhering to the official Android Architecture guidelines, employing **Clean Architecture** combined with **MVVM (Model-View-ViewModel)** and **Unidirectional Data Flow (UDF)**.

The system ensures:
- **Separation of Concerns**: UI, Domain, and Data layers are decoupled with strict dependency boundaries.
- **Offline First**: All transactional records, accounts, and insights reside locally in SQLite via Room.
- **Ledger Invariance**: Double-entry consistency and arbitrary precision `BigDecimal` arithmetic prevent fractional cent or rupee rounding discrepancies.
- **Auditability**: Every mutation to accounts, transactions, or budgets creates an append-only, immutable record in `audit_logs`.

```
┌─────────────────────────────────────────────────────────┐
│                     UI Layer                            │
│  - Jetpack Compose Screens & Reusable FinComponents      │
│  - Material Design 3 (Theming, Dynamic Colors)          │
│  - Navigation Compose                                   │
└────────────────────────────┬────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────┐
│                  ViewModel Layer                        │
│  - StateFlow UI States                                  │
│  - User Event Handling                                  │
│  - Coroutine Scopes (viewModelScope)                    │
└────────────────────────────┬────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────┐
│                   Domain Layer                          │
│  - Pure Kotlin Use Cases (e.g. CalculateNetWorth)       │
│  - Financial Models & Value Objects                     │
│  - Business Rules & Insight Engine                      │
│  - Repository Interfaces                                │
└────────────────────────────┬────────────────────────────┘
                             │
                             ▼
┌─────────────────────────────────────────────────────────┐
│                    Data Layer                           │
│  - Repository Implementations                           │
│  - Room Database (Entities, DAOs, TypeConverters)       │
│  - Atomic Database Transactions (@Transaction)          │
│  - DataStore Preferences (PIN, Currency, Themes)        │
│  - Exporters (CSV, JSON Backup, HTML/PDF Statements)    │
└─────────────────────────────────────────────────────────┘
```

## 2. Layer Deep Dive

### A. Data Layer (`com.fintrack.android.data`)
- **Entities**: Clean SQLite table mappings (`accounts`, `transactions`, `categories`, `budgets`, `savings_goals`, `debts`, `recurring_transactions`, `audit_logs`, `reconciliation_records`).
- **DAOs**: Reactive data streams using Kotlin Coroutines `Flow<List<T>>` for automatic real-time UI updates upon any database change.
- **Type Converters**: `RoomTypeConverters` serializes `BigDecimal` to `toPlainString()`, safeguarding exact precision.
- **Ledger Invariance & Balance Engine**:
  - `TransactionRepositoryImpl` executes inside `database.withTransaction { ... }`.
  - Adding an `INCOME` increments the account's balance.
  - Adding an `EXPENSE` decrements the account's balance.
  - An internal `TRANSFER` decrements the origin account and increments the destination account. **Transfers are strictly isolated and never counted as income or expense in summaries.**
  - Editing an existing transaction completely reverses the prior balance impact before applying the newly modified state.
  - Deleting a transaction rolls back the balance alteration and logs an immutable `AuditAction.DELETED`.

### B. Domain Layer (`com.fintrack.android.domain`)
- **Use Cases**:
  - `GetDashboardSummaryUseCase`: Computes real-time liquidity, income, expense, net savings, budget utilization, outstanding debts, and upcoming bills for any selected `TimeRange`.
  - `CalculateNetWorthUseCase`: Implements `Net Worth = Total Assets - Total Liabilities`. Integrates cash, bank, digital wallets, investments, and receivables (`OWED_TO_ME`), subtracted by borrowed loans (`OWED_BY_ME`) and credit balances.
  - `GenerateSpendingInsightsUseCase`: Rule-driven insight engine identifying highest spending categories, month-over-month variances, daily average spend, and budget limit thresholds.
  - `TransferFundsUseCase`: Validates non-identical accounts, positive amounts, and generates audited ledger transfer records.

### C. UI Layer (`com.fintrack.android.ui`)
- **Declarative Compose UI**:
  - Material 3 compliant styling with support for Light and Dark modes.
  - Custom canvas charts (`FinDonutChart`, `FinBarChart`, `FinLineChart`) delivering high-frame-rate visualizations without heavy third-party dependencies.
  - Reusable components (`FinTopBar`, `FinBottomBar`, `FinCard`, `FinAmountDisplay`, `FinConfirmationDialog`).

## 3. Dependency Injection
Built using **Hilt (Dagger)**:
- `DatabaseModule`: Provides singleton Room DB instance, DAOs, and CoroutineScope.
- `RepositoryModule`: Binds domain repository interfaces to concrete data implementations.

