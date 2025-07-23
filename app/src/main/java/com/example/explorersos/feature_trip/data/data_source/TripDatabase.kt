package com.example.explorersos.feature_trip.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.explorersos.feature_trip.domain.model.Alert
import com.example.explorersos.feature_trip.domain.model.AlertRecipient
import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.util.RoomTypeConverters

@Database(
    entities = [Trip::class, Alert::class, AlertRecipient::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class) // Add this line

abstract class TripDatabase : RoomDatabase() {
    abstract val tripDao: TripDao
    abstract val alertDao: AlertDao
    abstract val alertRecipientDao: AlertRecipientDao
    abstract val checkInDao: CheckInDao

    companion object {
        const val DATABASE_NAME = "trips_db"
    }
}
