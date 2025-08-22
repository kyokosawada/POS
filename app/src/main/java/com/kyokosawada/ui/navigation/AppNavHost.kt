package com.kyokosawada.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kyokosawada.ui.cart.CartView
import com.kyokosawada.ui.dashboard.DashboardView
import com.kyokosawada.ui.product.ProductListView
import com.kyokosawada.ui.settings.SettingsView
import com.kyokosawada.ui.transaction.TransactionHistoryView
import com.kyokosawada.ui.utils.WindowSizeClass
import com.kyokosawada.ui.utils.WindowWidthSizeClass

/**
 * Type-safe sealed class for defining navigation destinations.
 */
sealed class NavDestination(val route: String, val label: String, val icon: ImageVector) {
    object Dashboard : NavDestination("dashboard", "Home", Icons.Filled.Home)
    object Products : NavDestination("products", "Products", Icons.Filled.List)
    object Cart : NavDestination("cart", "Cart", Icons.Filled.ShoppingCart)
    object Transactions : NavDestination("transactions", "Receipts", Icons.Filled.List)
    object Settings : NavDestination("settings", "Settings", Icons.Filled.Settings)
}

val navItems = listOf(
    NavDestination.Dashboard,
    NavDestination.Products,
    NavDestination.Cart,
    NavDestination.Transactions,
    NavDestination.Settings
)

/**
 * Adaptive navigation scaffold. Decides bottom bar vs rail and renders main NavHost for app.
 */
@Composable
fun AppNavHost(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val isMediumOrExpanded =
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium || windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded

    Scaffold(
        bottomBar = {
            if (isCompact) {
                NavigationBar {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = { navController.navigate(item.route) },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        },
        content = { innerPadding ->
            Row(modifier = Modifier.fillMaxSize()) {
                if (isMediumOrExpanded) {
                    NavigationRail {
                        navItems.forEach { item ->
                            NavigationRailItem(
                                selected = currentRoute == item.route,
                                onClick = { navController.navigate(item.route) },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = NavDestination.Dashboard.route,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    composable(NavDestination.Dashboard.route) { DashboardView() }
                    composable(NavDestination.Products.route) { ProductListView() }
                    composable(NavDestination.Cart.route) { CartView(windowSizeClass = windowSizeClass) }
                    composable(NavDestination.Transactions.route) { TransactionHistoryView() }
                    composable(NavDestination.Settings.route) { SettingsView() }
                }
            }
        }
    )
}
