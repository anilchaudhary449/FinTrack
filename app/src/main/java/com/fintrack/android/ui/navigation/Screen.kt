package com.fintrack.android.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Dashboard : Screen("dashboard")
    object Transactions : Screen("transactions")
    object AddEditTransaction : Screen("add_transaction?id={id}&type={type}") {
        fun createRoute(id: Long? = null, type: String? = null): String {
            val idParam = id ?: -1L
            val typeParam = type ?: "EXPENSE"
            return "add_transaction?id=$idParam&type=$typeParam"
        }
    }
    object TransactionDetails : Screen("transaction_details/{id}") {
        fun createRoute(id: Long): String = "transaction_details/$id"
    }
    object Accounts : Screen("accounts")
    object Budgets : Screen("budgets")
    object AddEditBudget : Screen("add_budget?id={id}") {
        fun createRoute(id: Long? = null): String = "add_budget?id=${id ?: -1L}"
    }
    object Savings : Screen("savings")
    object AddSavingsGoal : Screen("add_savings")
    object Debts : Screen("debts")
    object AddDebt : Screen("add_debt")
    object Recurring : Screen("recurring")
    object AddRecurring : Screen("add_recurring")
    object Reports : Screen("reports")
    object Calendar : Screen("calendar")
    object NetWorth : Screen("networth")
    object Audit : Screen("audit")
    object Insights : Screen("insights")
    object Reconciliation : Screen("reconciliation/{accountId}") {
        fun createRoute(accountId: Long): String = "reconciliation/$accountId"
    }
    object Settings : Screen("settings")
}

