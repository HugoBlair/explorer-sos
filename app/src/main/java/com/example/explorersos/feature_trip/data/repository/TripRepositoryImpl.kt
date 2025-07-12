package com.example.explorersos.feature_trip.data.repository

import com.example.explorersos.feature_trip.data.data_source.TripDao
import com.example.explorersos.feature_trip.domain.model.Trip
import com.example.explorersos.feature_trip.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow

class TripRepositoryImpl(
    private val tripDao: TripDao

) : TripRepository {
    override fun getTrips(): Flow<List<Trip>> {
        return tripDao.getTrips()
    }

    override suspend fun getTripById(id: Int): Trip? {
        return tripDao.getTripById(id)
    }

    override suspend fun insertTrip(trip: Trip) {
        tripDao.insertTrip(trip)
    }

    override suspend fun deleteTrip(trip: Trip) {
        tripDao.deleteTrip(trip)
    }
}
