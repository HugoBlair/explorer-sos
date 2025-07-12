package com.example.explorersos.feature_trip.data.data_source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.explorersos.feature_trip.domain.model.Trip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var tripDao: TripDao
    private lateinit var db: TripDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TripDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tripDao = db.tripDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertTripAndGetById() = runTest {
        // Arrange
        val trip = Trip(
            id = 1,
            title = "Big Climb",
            destination = "Mt Kaukau",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )

        // Act
        tripDao.insertTrip(trip)
        val retrievedTrip = tripDao.getTripById(1)

        // Assert
        assertTrue(retrievedTrip != null)
        assertEquals(trip, retrievedTrip)
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleTripsAndGetAll() = runTest {
        // Arrange
        val trip1 = Trip(
            title = "Ridge traverse",
            destination = "Wainui Hill",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )
        val trip2 = Trip(
            title = "Mountaineering mish",
            destination = "Mt Ruapehu",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )

        // Act
        tripDao.insertTrip(trip1)
        tripDao.insertTrip(trip2)
        val allTrips = tripDao.getTrips().first()

        // Assert
        assertEquals(2, allTrips.size)
        val titles = allTrips.map { it.title }
        assertTrue(titles.contains("Ridge traverse") && titles.contains("Mountaineering mish"))
    }

    @Test
    @Throws(Exception::class)
    fun deleteTrip_deletesCorrectTrip() = runTest {
        // Arrange
        val tripToDelete = Trip(
            id = 5,
            title = "To Be Deleted",
            destination = "Nowhere",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )
        val tripToKeep = Trip(
            id = 6,
            title = "To Be Kept",
            destination = "Somewhere",
            startDate = "2025-01-03T00:00:00Z",
            endDate = "2025-01-04T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )
        tripDao.insertTrip(tripToDelete)
        tripDao.insertTrip(tripToKeep)

        // Act
        tripDao.deleteTrip(tripToDelete)
        val allTrips = tripDao.getTrips().first()
        val deletedTrip = tripDao.getTripById(5)

        // Assert
        assertTrue(deletedTrip == null)
        assertEquals(1, allTrips.size)
        assertEquals("To Be Kept", allTrips.first().title)
    }

    @Test
    @Throws(Exception::class)
    fun updateTrip_replacesOnConflict() = runTest {
        // Arrange
        val originalTrip = Trip(
            id = 10,
            title = "Original Title",
            destination = "Original Dest",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )
        tripDao.insertTrip(originalTrip)

        // Act
        val updatedTrip = Trip(
            id = 10,
            title = "Updated Title",
            destination = "Updated Dest",
            startDate = "2025-01-03T00:00:00Z",
            endDate = "2025-01-04T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )
        tripDao.insertTrip(updatedTrip)
        val retrievedTrip = tripDao.getTripById(10)

        // Assert
        assertTrue(retrievedTrip != null)
        if (retrievedTrip != null) {
            assertEquals("Updated Title", retrievedTrip.title)
            assertEquals("Updated Dest", retrievedTrip.destination)
        }
    }
}
