package com.example.explorersos.feature_trip.domain.use_case.check_in

import com.example.explorersos.feature_trip.domain.model.CheckIn
import com.example.explorersos.feature_trip.domain.repository.CheckInRepository

class DeleteCheckIn(
    private val repository: CheckInRepository
) {
    suspend operator fun invoke(checkIn: CheckIn) {
        repository.deleteCheckIn(checkIn)
    }
}
