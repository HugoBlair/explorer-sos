package com.example.explorersos.feature_trip.presentation.trips

import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.util.OrderType
import com.example.explorersos.feature_trip.domain.util.TripOrder

data class TripsState(
    val trips: List<Trip> = emptyList(),
    val tripOrder: TripOrder = TripOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val ongoingTrip: Trip? = null,
    val pastTrips: List<Trip> = emptyList()
)