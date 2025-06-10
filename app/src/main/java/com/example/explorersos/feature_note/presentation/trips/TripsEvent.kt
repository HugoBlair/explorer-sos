package com.example.explorersos.feature_note.presentation.trips

import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.util.TripOrder

sealed class TripsEvent {
    data class Order(val tripOrder: TripOrder) : TripsEvent()
    data class DeleteTrip(val trip: Trip) : TripsEvent()
    object RestoreTrip : TripsEvent()
    object ToggleOrderSection : TripsEvent()

}