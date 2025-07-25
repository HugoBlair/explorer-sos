package com.example.explorersos.feature_trip.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object TripsScreenRoute : Routes()

    @Serializable
    data object ContactsScreenRoute : Routes() // New

    @Serializable
    data object SettingsScreenRoute : Routes() // New

    @Serializable
    data class AddEditTripScreenRoute(
        val tripId: Int = -1
    )
}