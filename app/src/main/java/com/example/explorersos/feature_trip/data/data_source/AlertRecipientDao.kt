package com.example.explorersos.feature_trip.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertRecipientDao {
    @Query("SELECT * FROM alertRecipient")
    fun getAlertRecipients(): Flow<List<AlertRecipient>>

    @Query("SELECT * FROM alertRecipient WHERE id = :id")
    suspend fun getAlertRecipientById(id: Int): AlertRecipient?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlertRecipient(alertRecipient: AlertRecipient)

    @Delete
    suspend fun deleteAlertRecipient(alertRecipient: AlertRecipient)
}