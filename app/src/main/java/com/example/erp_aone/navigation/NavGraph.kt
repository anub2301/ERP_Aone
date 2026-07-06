package com.example.erp_aone.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.erp_aone.ui.screens.*
import com.example.erp_aone.viewmodel.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val loggedInUserId by authViewModel.loggedInUserId.collectAsState()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onContinue = {
                val next = if (loggedInUserId != null) "main" else "auth"
                navController.navigate(next) {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("auth") {
            AuthScreen(viewModel = authViewModel, onAuthSuccess = {
                navController.navigate("main") {
                    popUpTo("auth") { inclusive = true }
                }
            })
        }
        composable("main") {
            MainContainer(onLogout = {
                authViewModel.logout()
                navController.navigate("auth") {
                    popUpTo("main") { inclusive = true }
                }
            }, onReset = {
                authViewModel.resetData()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(onLogout: () -> Unit, onReset: () -> Unit) {
    val navController = rememberNavController()
    var showResetDialog by remember { mutableStateOf(false) }

    val items = listOf(
        BottomNavItem("Inventory", "inventory", Icons.Default.Inventory),
        BottomNavItem("Sales", "sales", Icons.Default.ShoppingCart),
        BottomNavItem("Sheet", "sheet", Icons.Default.Assessment),
        BottomNavItem("Customers", "customers", Icons.Default.People),
        BottomNavItem("Billing", "billing", Icons.Default.ReceiptLong)
    )

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Data") },
            text = { Text("Are you sure you want to erase ALL your data? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { onReset(); showResetDialog = false }) { Text("RESET", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text("CANCEL") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ERP_Aone", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showResetDialog = true }) { Icon(Icons.Default.DeleteForever, contentDescription = "Reset") }
                    IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, contentDescription = "Logout") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        val inventoryViewModel: InventoryViewModel = hiltViewModel()
        val salesViewModel: SalesViewModel = hiltViewModel()
        val monthlyViewModel: MonthlySheetViewModel = hiltViewModel()
        val customerViewModel: CustomerViewModel = hiltViewModel()
        val billingViewModel: BillingViewModel = hiltViewModel()

        NavHost(navController = navController, startDestination = "inventory", modifier = Modifier.padding(padding)) {
            composable("inventory") { InventoryScreen(inventoryViewModel) }
            composable("sales") { SalesScreen(salesViewModel, inventoryViewModel) }
            composable("sheet") { MonthlySheetScreen(monthlyViewModel) }
            composable("customers") { CustomerScreen(customerViewModel) }
            composable("billing") { BillingScreen(billingViewModel, customerViewModel, inventoryViewModel) }
        }
    }
}

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)
