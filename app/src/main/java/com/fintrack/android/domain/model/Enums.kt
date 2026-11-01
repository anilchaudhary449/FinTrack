package com.fintrack.android.domain.model

enum class CategoryType {
    INCOME,
    EXPENSE
}

enum class RecurrenceFrequency(val displayName: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    CUSTOM("Custom")
}

enum class DebtType {
    OWED_BY_ME, // Money I borrowed (Liability)
    OWED_TO_ME  // Money I lent (Asset)
}

enum class DebtStatus {
    ACTIVE,
    PARTIALLY_PAID,
    SETTLED,
    OVERDUE
}

enum class AuditAction {
    CREATED,
    UPDATED,
    DELETED,
    RECONCILED
}

enum class TimeRange {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    LAST_MONTH,
    THIS_YEAR,
    CUSTOM
}

