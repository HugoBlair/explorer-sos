package com.example.explorersos.feature_note.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object TripsScreenRoute : Routes()

    @Serializable
    data class AddEditTripScreenRoute(
        val tripId: Int = -1
    )
}