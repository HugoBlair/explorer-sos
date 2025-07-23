package com.example.explorersos.feature_trip.domain.use_case.trip

data class TripUseCases(
    val getTrips: GetTrips,
    val deleteTrip: DeleteTrip,
    val addTrip: AddTrip,
    val getTrip: GetTrip
)
