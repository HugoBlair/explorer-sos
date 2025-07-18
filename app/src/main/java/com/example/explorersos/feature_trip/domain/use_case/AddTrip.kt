package com.example.explorersos.feature_trip.domain.use_case

import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.repository.TripRepository

class AddTrip(
    private val repository: TripRepository
) {
    @Throws(Trip.InvalidTripException::class)
    suspend operator fun invoke(trip: Trip) {
        if (trip.title.isBlank()) {
            throw Trip.InvalidTripException("The title of the trip can't be empty.")
        }
        if (trip.description.isBlank()) {
            throw Trip.InvalidTripException("The content of the trip can't be empty.")

        }
        repository.insertTrip(trip)
    }
}