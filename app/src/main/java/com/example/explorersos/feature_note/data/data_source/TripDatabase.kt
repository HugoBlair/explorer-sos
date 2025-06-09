package com.example.explorersos.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.explorersos.feature_note.domain.model.Alert
import com.example.explorersos.feature_note.domain.model.AlertRecipient
import com.example.explorersos.feature_note.domain.model.Trip

@Database(
    entities = [Trip::class, Alert::class, AlertRecipient::class],
    version = 1,
    exportSchema = false
)

abstract class TripDatabase : RoomDatabase() {
    abstract val tripDao: TripDao

    companion object {
        const val DATABASE_NAME = "trips_db"
    }
}
