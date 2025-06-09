package com.example.explorersos.feature_note.domain.use_case

import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.repository.TripRepository

class GetTrip(
    private val repository: TripRepository

) {
    suspend operator fun invoke(id: Int): Trip? {
        return repository.getTripById(id)
    }

}