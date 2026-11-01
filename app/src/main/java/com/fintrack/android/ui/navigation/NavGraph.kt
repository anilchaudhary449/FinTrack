package com.fintrack.android.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.fintrack.android.domain.model.TransactionType
import com.fintrack.android.ui.components.FinBottomBar
import com.fintrack.android.ui.screens.account.AccountScreen
import com.fintrack.android.ui.screens.audit.AuditScreen
import com.fintrack.android.ui.screens.budget.BudgetScreen
import com.fintrack.android.ui.screens.calendar.CalendarScreen
import com.fintrack.android.ui.screens.dashboard.DashboardScreen
import com.fintrack.android.ui.screens.debt.DebtScreen
import com.fintrack.android.ui.screens.insights.InsightsScreen
import com.fintrack.android.ui.screens.networth.NetWorthScreen
import com.fintrack.android.ui.screens.onboarding.OnboardingScreen
import com.fintrack.android.ui.screens.reports.ReportsScreen
import com.fintrack.android.ui.screens.savings.SavingsScreen
import com.fintrack.android.ui.screens.settings.SettingsScreen
import com.fintrack.android.ui.screens.splash.SplashScreen
import com.fintrack.android.ui.screens.transaction.AddEditTransactionScreen
import com.fintrack.android.ui.screens.transaction.TransactionDetailsScreen
import com.fintrack.android.ui.screens.transaction.TransactionListScreen

@Composable
fun FinTrackNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Splash.route

    val bottomBarRoutes = listOf(
        Screen.Dashboard.route,
        Screen.Transactions.route,
        Screen.Budgets.route,
        Screen.Reports.route,
        Screen.Settings.route
    )
    val shouldShowBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                FinBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateNext = { dest ->
                        navController.navigate(dest) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinishOnboarding = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToAddTransaction = { type ->
                        navController.navigate(Screen.AddEditTransaction.createRoute(type = type.name))
                    },
                    onNavigateToTransactionDetails = { id ->
                        navController.navigate(Screen.TransactionDetails.createRoute(id))
                    },
                    onNavigateToTransactions = {
                        navController.navigate(Screen.Transactions.route)
                    },
                    onNavigateToNetWorth = {
                        navController.navigate(Screen.NetWorth.route)
                    },
                    onNavigateToInsights = {
                        navController.navigate(Screen.Insights.route)
                    },
                    onNavigateToCalendar = {
                        navController.navigate(Screen.Calendar.route)
                    }
                )
            }

            composable(Screen.Transactions.route) {
                TransactionListScreen(
                    onNavigateToAddTransaction = {
                        navController.navigate(Screen.AddEditTransaction.createRoute())
                    },
                    onNavigateToTransactionDetails = { id ->
                        navController.navigate(Screen.TransactionDetails.createRoute(id))
                    }
                )
            }

            composable(
                route = Screen.AddEditTransaction.route,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.LongType
                        defaultValue = -1L
                    },
                    navArgument("type") {
                        type = NavType.StringType
                        defaultValue = "EXPENSE"
                    }
                )
            ) {
                AddEditTransactionScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.TransactionDetails.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.LongType }
                )
            ) {
                TransactionDetailsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id ->
                        navController.navigate(Screen.AddEditTransaction.createRoute(id = id))
                    }
                )
            }

            composable(Screen.Budgets.route) {
                BudgetScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Reports.route) {
                ReportsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateToAccounts = { navController.navigate(Screen.Accounts.route) },
                    onNavigateToDebts = { navController.navigate(Screen.Debts.route) },
                    onNavigateToNetWorth = { navController.navigate(Screen.NetWorth.route) },
                    onNavigateToAudit = { navController.navigate(Screen.Audit.route) },
                    onNavigateToInsights = { navController.navigate(Screen.Insights.route) }
                )
            }

            composable(Screen.Accounts.route) {
                AccountScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Savings.route) {
                SavingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Debts.route) {
                DebtScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Calendar.route) {
                CalendarScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTransactionDetails = { id ->
                        navController.navigate(Screen.TransactionDetails.createRoute(id))
                    }
                )
            }

            composable(Screen.NetWorth.route) {
                NetWorthScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Audit.route) {
                AuditScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Insights.route) {
                InsightsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

