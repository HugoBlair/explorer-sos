package com.example.explorersos.feature_trip.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.explorersos.feature_trip.domain.model.CheckIn
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {
    @Update
    suspend fun updateCheckIn(checkIn: CheckIn)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckIn)

    @Delete
    suspend fun deleteCheckIn(checkIn: CheckIn)

    @Query("SELECT * FROM checkin WHERE tripId = :tripId ORDER BY dueDateTime ASC")
    fun getCheckInsForTrip(tripId: Int): Flow<List<CheckIn>>

    @Query("SELECT * FROM checkin WHERE status = 'PENDING' AND dueDateTime <= :currentTime")
    suspend fun getOverdueCheckIns(currentTime: Long): List<CheckIn>
}