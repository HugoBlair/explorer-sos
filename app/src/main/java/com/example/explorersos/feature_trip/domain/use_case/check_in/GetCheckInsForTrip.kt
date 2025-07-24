package com.example.explorersos.feature_trip.domain.use_case.check_in

import com.example.explorersos.feature_trip.domain.model.CheckIn
import com.example.explorersos.feature_trip.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow

class GetCheckInsForTrip(
    private val repository: CheckInRepository
) {
    operator fun invoke(tripId: Int): Flow<List<CheckIn>> {
        return repository.getCheckInsForTrip(tripId)
    }
}