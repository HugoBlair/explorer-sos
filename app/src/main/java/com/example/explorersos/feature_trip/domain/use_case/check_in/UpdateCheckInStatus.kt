package com.example.explorersos.feature_trip.domain.use_case.check_in

import com.example.explorersos.feature_trip.domain.model.CheckIn
import com.example.explorersos.feature_trip.domain.repository.CheckInRepository
import com.example.explorersos.feature_trip.domain.util.CheckInStatus

class UpdateCheckInStatus(
    private val repository: CheckInRepository
) {
    suspend operator fun invoke(checkIn: CheckIn, newStatus: CheckInStatus) {
        // Use .copy() to safely create a modified version of the object
        val updatedCheckIn = checkIn.copy(status = newStatus)
        repository.updateCheckIn(updatedCheckIn)
    }
}