package com.example.explorersos.feature_trip.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.explorersos.feature_trip.presentation.navigation.AppNavigation
import com.example.explorersos.feature_trip.presentation.navigation.BottomNavItem

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // List of routes that should show the bottom bar
            val bottomNavRoutes = listOf(
                BottomNavItem.Trips.route::class.qualifiedName,
                BottomNavItem.Contacts.route::class.qualifiedName,
                BottomNavItem.Alerts.route::class.qualifiedName,
                BottomNavItem.Settings.route::class.qualifiedName
            )

            // Show bottom bar only on the main screens
            if (currentDestination?.route in bottomNavRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Trips,
        BottomNavItem.Contacts,
        BottomNavItem.Alerts,
        BottomNavItem.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            val itemRouteName = item.route::class.qualifiedName
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                alwaysShowLabel = true,
                selected = currentDestination?.hierarchy?.any { it.route == itemRouteName } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}