# FinTrack - Database Schema Documentation

## 1. Overview
FinTrack utilizes a normalized SQLite database via Android's Room persistence library (`fintrack.db`, version 1).

### Key Design Principles:
1. **Arbitrary Monetary Precision**: All financial amounts are stored as exact numeric decimal strings (`toPlainString()`) and converted to `BigDecimal`. No floating-point formats (`REAL` or `DOUBLE`) are ever used for balances.
2. **Referential Integrity**: Foreign keys link transactions, budgets, debts, and savings to accounts and categories.
3. **Query Optimization**: High-frequency search fields (`accountId`, `categoryId`, `transactionDate`, `type`, `month`, `year`) are explicitly indexed.
4. **Append-Only Auditing**: `audit_logs` records mutations with timestamps and JSON state snapshots.

---

## 2. Table Specifications

### 1. `accounts`
Stores financial balances, wallets, and institutions.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Unique account ID |
| `name` | `TEXT` | NOT NULL | Account name (e.g., "Cash Wallet", "Nabil Bank") |
| `type` | `TEXT` | NOT NULL | `CASH_WALLET`, `BANK_ACCOUNT`, `DIGITAL_WALLET`, `SAVINGS_ACCOUNT`, `CREDIT_CARD`, `INVESTMENT`, `OTHER` |
| `openingBalance` | `TEXT` | NOT NULL | Initial balance at creation |
| `currentBalance` | `TEXT` | NOT NULL | Live ledger balance |
| `currency` | `TEXT` | NOT NULL DEFAULT 'NPR' | Currency code |
| `description` | `TEXT` | NOT NULL DEFAULT '' | Optional account notes |
| `isArchived` | `INTEGER` | NOT NULL DEFAULT 0 | Soft-deletion flag |
| `createdAt` | `INTEGER` | NOT NULL | Milliseconds since Unix epoch |
| `updatedAt` | `INTEGER` | NOT NULL | Timestamp of last modification |

---

### 2. `categories`
Categorization tree for income and expense classification.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Unique category ID |
| `name` | `TEXT` | NOT NULL | Category label (e.g., "Food & Groceries") |
| `type` | `TEXT` | NOT NULL | `INCOME` or `EXPENSE` |
| `iconName` | `TEXT` | NOT NULL | Material icon vector key |
| `colorHex` | `TEXT` | NOT NULL | Visual accent hex code |
| `parentCategoryId` | `INTEGER` | NULLABLE | Supports hierarchical subcategories |
| `isDefault` | `INTEGER` | NOT NULL DEFAULT 0 | Pre-seeded default indicator |
| `isFavorite` | `INTEGER` | NOT NULL DEFAULT 0 | Pinned quick-access category |
| `createdAt` | `INTEGER` | NOT NULL | Milliseconds since Unix epoch |

---

### 3. `transactions`
The central financial journal and double-entry transaction ledger.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Unique transaction ID |
| `type` | `TEXT` | NOT NULL | `INCOME`, `EXPENSE`, `TRANSFER`, `DEBT_LENT`, `DEBT_BORROWED`, `DEBT_REPAYMENT_MADE`, `DEBT_REPAYMENT_RECEIVED`, `SAVINGS_CONTRIBUTION`, `SAVINGS_WITHDRAWAL`, `RECONCILIATION_ADJUSTMENT` |
| `amount` | `TEXT` | NOT NULL | Monetary quantity (BigDecimal) |
| `categoryId` | `INTEGER` | NULLABLE, FK -> `categories(id)` ON DELETE SET NULL | Category classification |
| `accountId` | `INTEGER` | NOT NULL, FK -> `accounts(id)` ON DELETE RESTRICT | Primary or source account |
| `toAccountId` | `INTEGER` | NULLABLE | Destination account (Transfers only) |
| `paymentMethod` | `TEXT` | NOT NULL DEFAULT 'CASH' | `CASH`, `ESEWA`, `KHALTI`, `FONEPAY`, `BANK_TRANSFER`, `CREDIT_CARD`, `DEBIT_CARD`, `CHEQUE`, `OTHER` |
| `transactionDate` | `INTEGER` | NOT NULL | Timestamp when transaction occurred |
| `notes` | `TEXT` | NOT NULL DEFAULT '' | Description, merchant or notes |
| `receiptUri` | `TEXT` | NULLABLE | File URI to captured receipt photo |
| `currency` | `TEXT` | NOT NULL DEFAULT 'NPR' | Currency of transaction |
| `exchangeRate` | `TEXT` | NOT NULL DEFAULT '1.0' | Historical conversion rate |
| `recurringTransactionId` | `INTEGER` | NULLABLE | Linked schedule ID if auto-generated |
| `debtId` | `INTEGER` | NULLABLE | Linked loan/debt record |
| `savingsGoalId` | `INTEGER` | NULLABLE | Linked savings target |
| `isReconciled` | `INTEGER` | NOT NULL DEFAULT 0 | Verified during bank reconciliation |
| `createdAt` | `INTEGER` | NOT NULL | Timestamp inserted |
| `updatedAt` | `INTEGER` | NOT NULL | Timestamp last edited |

**Indices:**
- `INDEX(accountId)`
- `INDEX(toAccountId)`
- `INDEX(categoryId)`
- `INDEX(transactionDate)`
- `INDEX(type)`

---

### 4. `budgets`
Monthly spending ceilings with configurable warning thresholds.
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Unique budget ID |
| `name` | `TEXT` | NOT NULL | Title of budget |
| `categoryId` | `INTEGER` | NULLABLE, FK -> `categories(id)` | Targeted category (null = overall budget) |
| `amountLimit` | `TEXT` | NOT NULL | Spending cap amount |
| `warningThresholdPercentage` | `INTEGER` | NOT NULL DEFAULT 80 | Percentage at which warning triggers |
| `month` | `INTEGER` | NOT NULL | Month (1 - 12) |
| `year` | `INTEGER` | NOT NULL | Gregorian Year |
| `startDate` | `INTEGER` | NOT NULL | Start epoch millis |
| `endDate` | `INTEGER` | NOT NULL | End epoch millis |
| `createdAt` | `INTEGER` | NOT NULL | Timestamp created |

---

### 5. `savings_goals` & `savings_contributions`
Goal tracking and dedicated savings deposits.
- `savings_goals`: `id`, `title`, `description`, `targetAmount`, `currentSavedAmount`, `targetDate`, `isCompleted`, `iconName`, `colorHex`, `createdAt`.
- `savings_contributions`: `id`, `savingsGoalId` (FK), `accountId` (FK), `amount`, `contributionDate`, `notes`.

---

### 6. `debts` & `debt_payments`
Tracks borrowed loans and receivables.
- `debts`: `id`, `personName`, `type` (`OWED_BY_ME` or `OWED_TO_ME`), `originalAmount`, `outstandingAmount`, `interestRate`, `dueDate`, `status` (`ACTIVE`, `PARTIALLY_PAID`, `SETTLED`, `OVERDUE`), `notes`, `contactNumber`, `accountId`, `createdAt`, `updatedAt`.
- `debt_payments`: `id`, `debtId` (FK), `accountId` (FK), `amount`, `paymentDate`, `notes`.

---

### 7. `recurring_transactions`
Scheduled automated income, rent, utilities, and installments.
- `recurring_transactions`: `id`, `title`, `type`, `amount`, `categoryId` (FK), `accountId` (FK), `paymentMethod`, `frequency` (`DAILY`, `WEEKLY`, `MONTHLY`, `YEARLY`, `CUSTOM`), `intervalValue`, `startDate`, `endDate`, `nextDueDate`, `lastProcessedDate`, `isActive`, `autoCreate`, `notes`, `createdAt`.

---

### 8. `audit_logs`
Immutable financial audit trail.
- `audit_logs`: `id`, `entityType`, `entityId`, `action` (`CREATED`, `UPDATED`, `DELETED`, `RECONCILED`), `description`, `previousStateJson`, `newStateJson`, `timestamp`.

---

### 9. `reconciliation_records`
Bank and wallet reconciliation history.
- `reconciliation_records`: `id`, `accountId` (FK), `reconciliationDate`, `appCalculatedBalance`, `actualStatementBalance`, `discrepancyAmount`, `adjustmentTransactionId`, `notes`.

