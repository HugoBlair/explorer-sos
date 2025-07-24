package com.example.explorersos.feature_trip.domain.use_case.check_in

import com.example.explorersos.feature_trip.domain.model.CheckIn
import com.example.explorersos.feature_trip.domain.repository.CheckInRepository

class AddCheckIn(private val checkInRepository: CheckInRepository) {
    suspend operator fun invoke(checkIn: CheckIn) {
        checkInRepository.insertCheckIn(checkIn)
    }


}