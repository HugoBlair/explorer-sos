package com.example.explorersos.feature_note.data.repository

import com.example.explorersos.feature_note.data.data_source.TripDao
import com.example.explorersos.feature_note.domain.model.Trip
import com.example.explorersos.feature_note.domain.repository.TripRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TripRepositoryImplTest {

    private lateinit var mockTripDao: TripDao
    private lateinit var tripRepository: TripRepository

    @BeforeEach
    fun setUp() {
        // Create a mock instance of TripDao
        mockTripDao = mockk()
        // Create the repository instance with the mock DAO
        tripRepository = TripRepositoryImpl(mockTripDao)
    }

    @Test
    fun `getTrips should call dao and return flow of trips`() = runTest {
        val testTrips = listOf(
            Trip(
                title = "Big Climb",
                destination = "Mt Kaukau",
                startDate = "01/01/2025",
                endDate = "02/01/2025",
                createdAt = 1L
            ),
            Trip(
                title = "Ridge traverse",
                destination = "Wainui Hill",
                startDate = "01/01/2025",
                endDate = "02/01/2025",
                createdAt = 2L
            )
        )
        val tripsFlow = flowOf(testTrips)
        every { mockTripDao.getTrips() } returns tripsFlow

        // Act: Call the repository method
        val resultFlow = tripRepository.getTrips()
        val resultList = resultFlow.first()

        assertEquals(testTrips, resultList)
        verify(exactly = 1) { mockTripDao.getTrips() }
    }

    @Test
    fun `getTripById should call dao and return correct trip`() = runTest {
        // Arrange
        val testTrip = Trip(
            id = 1,
            title = "Test Trip",
            destination = "Test Dest",
            startDate = "d1",
            endDate = "d2",
            createdAt = 1L
        )
        coEvery { mockTripDao.getTripById(1) } returns testTrip
        coEvery { mockTripDao.getTripById(99) } returns null

        // Act
        val foundTrip = tripRepository.getTripById(1)
        val notFoundTrip = tripRepository.getTripById(99)

        // Assert
        assertEquals(testTrip, foundTrip)
        assertEquals(null, notFoundTrip)
        coVerify(exactly = 1) { mockTripDao.getTripById(1) }
        coVerify(exactly = 1) { mockTripDao.getTripById(99) }
    }

    @Test
    fun `insertTrip should call dao's insertTrip`() = runTest {
        // Arrange
        val newTrip = Trip(
            title = "New Trip",
            destination = "New Dest",
            startDate = "d1",
            endDate = "d2",
            createdAt = 1L
        )
        coEvery { mockTripDao.insertTrip(any()) } returns Unit

        // Act
        tripRepository.insertTrip(newTrip)

        coVerify(exactly = 1) { mockTripDao.insertTrip(newTrip) }
    }

    @Test
    fun `deleteTrip should call dao's deleteTrip`() = runTest {
        // Arrange
        val tripToDelete = Trip(
            id = 1,
            title = "Delete Me",
            destination = "Bye",
            startDate = "d1",
            endDate = "d2",
            createdAt = 1L
        )
        coEvery { mockTripDao.deleteTrip(any()) } returns Unit

        // Act
        tripRepository.deleteTrip(tripToDelete)

        // Assert
        coVerify(exactly = 1) { mockTripDao.deleteTrip(tripToDelete) }
    }
}