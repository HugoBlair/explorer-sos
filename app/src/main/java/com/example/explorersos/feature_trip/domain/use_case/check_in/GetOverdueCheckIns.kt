package com.example.explorersos.feature_trip.domain.use_case.check_in

import com.example.explorersos.feature_trip.domain.model.CheckIn
import com.example.explorersos.feature_trip.domain.repository.CheckInRepository
import java.time.Instant

class GetOverdueCheckIns(
    private val repository: CheckInRepository
) {
    suspend operator fun invoke(): List<CheckIn> {
        // This use case encapsulates the logic of getting the current time.
        return repository.getOverdueCheckIns(Instant.now())
    }
}