# FinTrack (फिनट्र्याक)
### Comprehensive Personal Finance, Expense Management & Net Worth Tracking Application for Android

[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-green.svg)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin%202.0-blue.svg)](https://kotlinlang.org)
[![UI Framework](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)](https://developer.android.com/topic/architecture)
[![Database](https://img.shields.io/badge/Database-Room%20(SQLite)-purple.svg)](https://developer.android.com/training/data-storage/room)
[![Currency](https://img.shields.io/badge/Default%20Currency-NPR%20(%E0%A4%B0%E0%A5%81)-teal.svg)](#nepal-specific-customization)

FinTrack is an offline-first, production-grade personal financial management Android application. Designed from the ground up to maintain complete mathematical accuracy, immutable auditability, and data ownership, it provides comprehensive tools for income and expense tracking, multi-account ledger balancing, category budgeting, savings goals, loan amortization, net worth management, intelligent spending insights, and bank reconciliation.

---

## 🌟 Key Capabilities & Architectural Modules

### 1. 📊 Financial Dashboard
- **Real-Time Liquidity**: Instant visibility of total available balances across all cash, bank, and digital wallet accounts.
- **Dynamic Time Range Filtering**: Toggle between **Today**, **This Week**, **This Month**, **Last Month**, **This Year**, or custom date intervals.
- **Interactive Visualizations**:
  - **Donut Chart**: Category-wise expense breakdown with proportional percentage distribution.
  - **Bar Chart**: Income vs Expense comparative cash flow analysis.
  - **Trends & Forecasts**: Visual financial trajectory over time.
- **Quick Action Bar**: One-tap recording for Income, Expenses, and Transfers.

### 2. 💸 Advanced Expense & Income Management
- **High-Velocity Recording**: Rapid entry with keyboard decimal formatting and smart defaults.
- **Accurate Monetary Precision**: Built strictly using `BigDecimal` arithmetic with banker's rounding (`RoundingMode.HALF_EVEN`). Zero floating-point rounding errors.
- **Attachments**: Associate camera photos and gallery receipts with any transaction.
- **Transaction Search & Filtering**: Multi-parameter search across notes, merchant descriptions, categories, payment methods, accounts, and amount ranges.
- **Ledger Reversal**: Editing or deleting any transaction atomically reverses its historical balance impact without corrupting accounts.

### 3. 🏦 Multi-Account Ledger & Transfers
- **Account Types**: Cash Wallet, Commercial Bank Accounts, Digital Wallets (eSewa, Khalti, Fonepay), Savings Accounts, Credit Cards, and Investments.
- **Inter-Account Transfers**: Move funds between accounts (e.g. Bank to eSewa) without corrupting income or expense reports.
- **Account Reconciliation**: Match ledger balances with official bank statements, record discrepancies, and log adjustment entries.

### 4. 🎯 Budget Management
- **Threshold Alerts**: Color-coded progress indicators:
  - **Normal**: Under 50% consumption (Green).
  - **Warning**: Over 80% consumption (Amber).
  - **Exceeded**: Above 100% consumption (Red).
- **Targeted & Global**: Support for category-specific limits (e.g. Food, Fuel, Entertainment) and global monthly budgets.

### 5. 💎 Net Worth Module
- **Formula**: `Net Worth = Total Assets - Total Liabilities`
- **Assets**: Physical cash, bank accounts, digital wallets, savings goals, investments, and money lent to others (`OWED_TO_ME`).
- **Liabilities**: Outstanding borrowed loans (`OWED_BY_ME`), credit card balances, and personal debts.
- Avoids double-counting while preserving an accurate balance sheet.

### 6. 🤝 Debts & Loans Manager
- Separate tracking for **Money I Owe** (Liabilities) and **Money Owed to Me** (Assets).
- Support for interest rates, due dates, contact details, and partial or full repayments.
- Repayments update balances and debts without falsely inflating income or expenses.

### 7. ⏱️ Recurring Transactions & Background Worker
- Automated schedules for salaries, rent, utility bills, subscriptions, and loan installments.
- Recurrence frequencies: Daily, Weekly, Monthly, Yearly, Custom intervals.
- **WorkManager**: Background synchronization automatically executes due transactions without user intervention and prevents duplicate entries.

### 8. 🔍 Financial Audit Trail
- **Immutable Log**: All record creations, modifications, deletions, and balance reconciliations are persisted in an append-only `audit_logs` entity.
- Captures full previous state and new state JSON snapshots with precise millisecond timestamps.

### 9. 💡 Intelligent Spending Insights Engine
- Algorithmic analysis of recorded financial data:
  - Detection of highest spending categories.
  - Month-over-month spending increases/decreases.
  - Daily average spending calculation.
  - Budget ceiling warnings.

### 10. 🇳🇵 Nepal-Specific Customization
- **Currency**: Nepalese Rupee (NPR `रु`) configured as default.
- **Bilingual Support**: English and complete Nepali (`values-ne/strings.xml`) translations.
- **Payment Methods**: Pre-configured support for **Cash**, **eSewa**, **Khalti**, **Fonepay**, **Bank Transfer**, **Credit Card**, and custom options.
- **Calendar**: Integrated Gregorian to **Bikram Sambat (वि.सं.)** date converter.

### 11. 📦 Data Portability & Privacy
- **Full Database Backup**: Export and restore verified local JSON backups protected by SHA-256 cryptographic checksums.
- **CSV Export**: Standardized comma-separated values export compatible with Excel and Google Sheets.
- **Printable Statements**: Built-in HTML/PDF monthly statement generator.
- **Security**: Hardware-backed biometric authentication and application PIN lock.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **OS & Platform** | Android 8.0 (API 26) through Android 15 / 16 (API 35/36) |
| **Language** | Kotlin 2.0.21 |
| **UI Framework** | Jetpack Compose with Material Design 3 |
| **Architecture** | MVVM + Clean Architecture + Unidirectional Data Flow |
| **Local Database** | Room Database 2.6.1 (SQLite) with KSP compiler |
| **Dependency Injection** | Hilt (Dagger) 2.51.1 |
| **Asynchronous Engine** | Kotlin Coroutines & Reactive Flow |
| **Navigation** | Navigation Compose 2.8.3 |
| **Preferences Storage** | Jetpack DataStore Preferences |
| **Background Processing** | WorkManager 2.9.1 |
| **Image Loading** | Coil Compose 2.7.0 |
| **Biometrics** | AndroidX Biometric 1.2.0 |
| **Testing** | JUnit 4, MockK, AndroidX Arch Core Testing, Compose UI Testing |

---

## 📂 Project Directory Structure

```
com.fintrack.android/
├── data/
│   ├── export/               # CSV, JSON Backup, HTML/PDF Generator
│   ├── local/
│   │   ├── converters/       # Room TypeConverters (BigDecimal, Enums)
│   │   ├── dao/              # AccountDao, TransactionDao, BudgetDao, etc.
│   │   ├── entity/           # AccountEntity, TransactionEntity, AuditLog, etc.
│   │   └── AppDatabase.kt    # Room Database with automated seeding
│   ├── preferences/          # UserPreferencesManager (DataStore)
│   └── repository/           # Repository Implementations (Ledger Engine)
├── di/                       # Hilt Dependency Injection Modules
├── domain/
│   ├── model/                # Clean Kotlin domain models & enums
│   ├── repository/           # Repository Interfaces
│   ├── usecase/              # Business Logic & Financial Calculators
│   └── util/                 # CurrencyFormatter, BikramSambatConverter
├── ui/
│   ├── components/           # Custom Canvas Charts, Cards, Bars, Dialogs
│   ├── navigation/           # Screen routes & NavGraph
│   ├── screens/
│   │   ├── account/          # Account list, Transfer dialog, Reconciliation
│   │   ├── audit/            # Financial Audit Trail inspector
│   │   ├── budget/           # Budget limits & progress monitoring
│   │   ├── calendar/         # Financial Calendar view
│   │   ├── dashboard/        # Main Dashboard, Summary, Recent list
│   │   ├── debt/             # Debts, Loans & Repayments
│   │   ├── insights/         # Spending Insights engine UI
│   │   ├── networth/         # Net Worth calculator & breakdown
│   │   ├── onboarding/       # Currency onboarding setup
│   │   ├── reports/          # Financial Analytics, Charts & Export
│   │   ├── savings/          # Savings goals & contributions
│   │   ├── settings/         # Preferences, PIN, Backup & Restore
│   │   ├── splash/           # Launch branding & routing
│   │   └── transaction/      # Add, Edit, List, Search & Details
│   └── theme/                # Material 3 Color schemes, Shapes, Typography
└── worker/                   # WorkManager Recurring Transaction Worker
```

---

## 🚀 Setup & Build Instructions

### 1. Prerequisites
- **Android Studio**: Ladybug (2024.2+) or newer.
- **JDK**: Java Development Kit version 17 or higher.
- **Android SDK**: Compile SDK 35, Build Tools 36.1.0 or 35.0.0.

### 2. Opening in Android Studio
1. Clone or open the repository folder in Android Studio:
   `File` -> `Open...` -> Select `d:\finan_log`
2. Wait for Gradle sync to complete. The version catalog (`gradle/libs.versions.toml`) will resolve all dependencies.

### 3. Running Unit Tests
Execute the comprehensive test suite verifying financial arithmetic, ledger reversals, and transfers:
```powershell
.\gradlew.bat testDebugUnitTest
```
Or on Linux/macOS:
```bash
./gradlew testDebugUnitTest
```

### 4. Running the Application
Connect an Android physical device with USB debugging enabled, or launch an Android Virtual Device (AVD Emulator running API 26+), and press **Run** (Shift + F10) in Android Studio.

### 5. Building Release APK
Refer to [`docs/SIGNED_APK_INSTRUCTIONS.md`](docs/SIGNED_APK_INSTRUCTIONS.md) for detailed release signing and APK generation instructions:
```powershell
.\gradlew.bat assembleRelease
```

---

## 📚 Detailed Documentation Links
- [System Architecture Documentation](docs/ARCHITECTURE.md)
- [Complete Database Schema & ERD](docs/DATABASE_SCHEMA.md)
- [Signed APK & Play Store Bundle Instructions](docs/SIGNED_APK_INSTRUCTIONS.md)
- [Limitations & Future Roadmap](docs/LIMITATIONS_AND_FUTURE.md)

---

## 📄 License
This project is open-source under the Apache License 2.0.
