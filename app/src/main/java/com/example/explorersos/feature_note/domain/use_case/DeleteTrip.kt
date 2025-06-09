package com.example.explorersos.feature_note.domain.use_case

import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.repository.TripRepository

class DeleteTrip(
    private val repository: TripRepository
) {
    suspend operator fun invoke(trip: Trip) {
        repository.deleteTrip(trip)
    }

}