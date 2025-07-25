package com.example.explorersos.feature_trip.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: Routes,
    val title: String,
    val icon: ImageVector
) {
    data object Trips : BottomNavItem(Routes.TripsScreenRoute, "Trips", Icons.Default.Home)
    data object Contacts :
        BottomNavItem(Routes.ContactsScreenRoute, "Contacts", Icons.Default.Contacts)

    data object Settings :
        BottomNavItem(Routes.SettingsScreenRoute, "Settings", Icons.Default.Settings)
}