package com.example.explorersos.feature_note.presentation.trips

import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.util.OrderType
import com.example.explorersos.feature_note.domain.util.TripOrder

data class TripsState(
    val trips: List<Trip> = emptyList(),
    val tripOrder: TripOrder = TripOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)