package com.prakash.expensetracker.android

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.prakash.expensetracker.android.feature.add_expense.AddExpense
import com.prakash.expensetracker.android.feature.home.HomeScreen
import com.prakash.expensetracker.android.feature.stats.StatsScreen
import com.prakash.expensetracker.android.ui.theme.Zinc
import com.prakash.expensetracker.android.feature.biometric.BiometricLoginScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavHostScreen(navController: NavController) {  // Take navController as a parameter
    var bottomBarVisibility by remember {
        mutableStateOf(true)
    }
    Scaffold(bottomBar = {
        AnimatedVisibility(visible = bottomBarVisibility) {
            NavigationBottomBar(
                navController = navController,
                items = listOf(
                    NavItem(route = "/home", icon = R.drawable.ic_home),
                    NavItem(route = "/stats", icon = R.drawable.ic_stats)
                )
            )
        }
    }) {
        NavHost(
            navController = navController as NavHostController,
            startDestination = "/biometric",  // Start with the BiometricLoginScreen
            modifier = Modifier.padding(it)
        ) {
            composable(route = "/biometric") {
                bottomBarVisibility = false
                BiometricLoginScreen(navController = navController) // Navigate after authentication
            }
            composable(route = "/home") {
                bottomBarVisibility = true
                HomeScreen(navController)
            }
            composable(route = "/add") {
                bottomBarVisibility = false
                AddExpense(navController)
            }
            composable(route = "/stats") {
                bottomBarVisibility = true
                StatsScreen(navController)
            }
        }
    }
}

data class NavItem(
    val route: String,
    val icon: Int
)

@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Zinc,
                    selectedIconColor = Zinc,
                    unselectedTextColor = Color.Gray,
                    unselectedIconColor = Color.Gray
                )
            )
        }
    }
}
