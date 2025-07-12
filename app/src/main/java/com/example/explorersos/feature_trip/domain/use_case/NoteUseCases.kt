package com.example.explorersos.feature_trip.domain.use_case

data class TripUseCases(
    val getTrips: GetTrips,
    val deleteTrip: DeleteTrip,
    val addTrip: AddTrip,
    val getTrip: GetTrip
)
