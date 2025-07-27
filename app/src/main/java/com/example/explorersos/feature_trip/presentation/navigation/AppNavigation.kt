package com.example.explorersos.feature_trip.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.explorersos.feature_trip.presentation.add_edit_trip.AddEditTripScreen
import com.example.explorersos.feature_trip.presentation.contacts.ContactsScreen
import com.example.explorersos.feature_trip.presentation.settings.SettingsScreen
import com.example.explorersos.feature_trip.presentation.trips.TripsScreen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Routes.TripsScreenRoute,
        modifier = modifier
    ) {
        composable<Routes.TripsScreenRoute> {
            TripsScreen(
                navController = navController,
                viewModel = koinViewModel()
            )
        }

        composable<Routes.AddEditTripScreenRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Routes.AddEditTripScreenRoute>()
            AddEditTripScreen(
                navController = navController,
                viewModel = koinViewModel(),
                tripId = args.tripId
            )
        }

        composable<Routes.ContactsScreenRoute> {
            ContactsScreen(navController = navController, viewModel = koinViewModel())
        }

        composable<Routes.SettingsScreenRoute> {
            SettingsScreen()
        }

    }
}