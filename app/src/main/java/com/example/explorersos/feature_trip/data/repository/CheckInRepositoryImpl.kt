package com.example.explorersos.feature_trip.data.repository

import com.example.explorersos.feature_trip.data.data_source.CheckInDao
import com.example.explorersos.feature_trip.domain.model.CheckIn
import com.example.explorersos.feature_trip.domain.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow
import java.time.Instant

/**
 * Concrete implementation of the CheckInRepository.
 * It fulfills the contract by delegating calls to the local Room database via the CheckInDao.
 */
class CheckInRepositoryImpl(
    private val checkInDao: CheckInDao
) : CheckInRepository {

    override fun getCheckInsForTrip(tripId: Int): Flow<List<CheckIn>> {
        return checkInDao.getCheckInsForTrip(tripId)
    }

    override suspend fun insertCheckIn(checkIn: CheckIn) {
        checkInDao.insertCheckIn(checkIn)
    }

    override suspend fun updateCheckIn(checkIn: CheckIn) {
        checkInDao.updateCheckIn(checkIn)
    }

    override suspend fun deleteCheckIn(checkIn: CheckIn) {
        checkInDao.deleteCheckIn(checkIn)
    }

    override suspend fun getOverdueCheckIns(currentTime: Instant): List<CheckIn> {
        //  conversion from the clean `Instant` type
        // to the primitive `Long` that the DAO expects.
        val currentTimeMillis = currentTime.toEpochMilli()
        return checkInDao.getOverdueCheckIns(currentTimeMillis)
    }
}