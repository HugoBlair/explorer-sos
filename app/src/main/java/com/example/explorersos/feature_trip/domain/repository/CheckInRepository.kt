package com.example.explorersos.feature_trip.domain.repository

import com.example.explorersos.feature_trip.domain.model.CheckIn
import kotlinx.coroutines.flow.Flow

interface CheckInRepository {
    fun getCheckIns(): Flow<List<CheckIn>>

    fun getCheckInById(id: Int): CheckIn?

    fun updateCheckIn(checkIn: CheckIn)
    
    fun insertCheckIn(checkIn: CheckIn)

    fun deleteCheckIn(checkIn: CheckIn)
}