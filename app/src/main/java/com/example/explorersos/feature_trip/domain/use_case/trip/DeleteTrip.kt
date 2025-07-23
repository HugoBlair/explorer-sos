package com.example.explorersos.feature_trip.domain.use_case.trip

import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.repository.TripRepository

class DeleteTrip(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip) {
        repository.deleteTrip(trip)
    }

}