package com.example.explorersos.feature_trip.domain.use_case.trip

import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.repository.TripRepository

class AddTrip(
    private val repository: TripRepository
) {
    @Throws(Trip.InvalidTripException::class)
    suspend operator fun invoke(trip: Trip) {
        repository.insertTrip(trip)
    }
}