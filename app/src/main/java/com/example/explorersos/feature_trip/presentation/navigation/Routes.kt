package com.example.explorersos.feature_trip.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object TripsScreenRoute : Routes()

    @Serializable
    data object ContactsScreenRoute : Routes()

    @Serializable
    data object SettingsScreenRoute : Routes()

    @Serializable
    data class AddEditTripScreenRoute(
        val tripId: Int = -1
    )

    @Serializable
    data class AddEditContactScreenRoute(
        val contactId: Int = -1
    )

    @Serializable
    data class AddEditAlertScreenRoute(
        val alertId: Int = -1
    )
    
    @Serializable
    data object AlertsScreenRoute : Routes()


}