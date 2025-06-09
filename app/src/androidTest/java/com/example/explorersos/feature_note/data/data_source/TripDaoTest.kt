package com.example.explorersos.feature_note.data.data_source

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4 // Import the runner
import com.example.explorersos.feature_note.domain.model.Trip
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After // Import JUnit 4
import org.junit.Assert.assertEquals // Import JUnit 4
import org.junit.Assert.assertTrue // Import JUnit 4
import org.junit.Before // Import JUnit 4
import org.junit.Test // Import JUnit 4
import org.junit.runner.RunWith // Import RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class) // <-- IMPORTANT: Tell the system to use the Android Test Runner
class TripDaoTest {

    private lateinit var tripDao: TripDao
    private lateinit var db: TripDatabase

    @Before // <-- CHANGED from @BeforeEach
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TripDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        tripDao = db.tripDao
    }

    @After // <-- CHANGED from @AfterEach
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test // <-- No change needed here, just ensure import is org.junit.Test
    @Throws(Exception::class)
    fun insertTripAndGetById() = runTest {
        // Arrange
        val trip = Trip(
            id = 1, // Explicitly set ID for easy retrieval
            title = "Big Climb",
            destination = "Mt Kaukau",
            startDate = "01/01/2025",
            endDate = "02/01/2025",
            createdAt = System.currentTimeMillis()
        )

        // Act
        tripDao.insertTrip(trip)
        val retrievedTrip = tripDao.getTripById(1)

        // Assert
        assertTrue(retrievedTrip != null)
        assertEquals(
            trip,
            retrievedTrip
        ) // <-- CHANGED from assertEquals(retrievedTrip, trip) for better failure messages
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleTripsAndGetAll() = runTest {
        // Arrange
        val trip1 = Trip(
            title = "Ridge traverse",
            destination = "Wainui Hill",
            startDate = "01/01/2025",
            endDate = "02/01/2025",
            createdAt = System.currentTimeMillis()
        )
        val trip2 = Trip(
            title = "Mountaineering mish",
            destination = "Mt Ruapehu",
            startDate = "01/01/2025",
            endDate = "02/01/2025",
            createdAt = System.currentTimeMillis()
        )

        // Act
        tripDao.insertTrip(trip1)
        tripDao.insertTrip(trip2)
        val allTrips = tripDao.getTrips().first() // Collect the first list emitted by the flow

        // Assert
        assertEquals(2, allTrips.size) // <-- CHANGED from assertTrue(allTrips.size == 2)
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
            startDate = "d1",
            endDate = "d2",
            createdAt = 1L
        )
        val tripToKeep = Trip(
            id = 6,
            title = "To Be Kept",
            destination = "Somewhere",
            startDate = "d3",
            endDate = "d4",
            createdAt = 2L
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
            startDate = "d1",
            endDate = "d2",
            createdAt = 1L
        )
        tripDao.insertTrip(originalTrip)

        // Act
        val updatedTrip = Trip(
            id = 10,
            title = "Updated Title",
            destination = "Updated Dest",
            startDate = "d3",
            endDate = "d4",
            createdAt = 1L
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