package com.example.explorersos.feature_note.domain.respository

import com.example.explorersos.feature_note.domain.model.Trip
import kotlinx.coroutines.flow.Flow

interface TripRepository {

    fun getTrips(): Flow<List<Trip>>

    suspend fun getTripById(id: Int): Trip?

    suspend fun insertTrip(trip: Trip)

    suspend fun deleteTrip(trip: Trip)
}