package com.example.explorersos.feature_trip.domain.use_case.trip

import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.repository.TripRepository

class GetTrip(
    private val repository: TripRepository

) {
    suspend operator fun invoke(id: Int): Trip? {
        return repository.getTripById(id)
    }

}