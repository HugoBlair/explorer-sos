package com.example.explorersos.feature_trip.presentation.trips

import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.util.TripOrder

sealed class TripsEvent {
    data class Order(val tripOrder: TripOrder) : TripsEvent()
    data class DeleteTrip(val trip: Trip) : TripsEvent()
    object RestoreTrip : TripsEvent()
    object ToggleOrderSection : TripsEvent()
    data class EndTrip(val trip: Trip) : TripsEvent()
    object RestoreEndedTrip : TripsEvent()


}