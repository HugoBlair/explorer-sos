package com.example.explorersos.feature_trip.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.model.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alert")
    fun getAlerts(): Flow<List<Alert>>

    @Query("SELECT * FROM alert WHERE id = :id")
    suspend fun getAlertById(id: Int): Trip?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert)

    @Delete
    suspend fun deleteAlert(alert: Alert)
}