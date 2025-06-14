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
                startDate = "2025-01-01T08:00:00Z",
                endDate = "2025-01-02T08:00:00Z",
                createdAt = "2024-12-31T18:00:00Z"
            ),
            Trip(
                title = "Ridge traverse",
                destination = "Wainui Hill",
                startDate = "2025-01-01T08:00:00Z",
                endDate = "2025-01-02T08:00:00Z",
                createdAt = "2024-12-31T19:00:00Z"
            )
        )
        val tripsFlow = flowOf(testTrips)
        every { mockTripDao.getTrips() } returns tripsFlow

        val resultFlow = tripRepository.getTrips()
        val resultList = resultFlow.first()

        assertEquals(testTrips, resultList)
        verify(exactly = 1) { mockTripDao.getTrips() }
    }

    @Test
    fun `getTripById should call dao and return correct trip`() = runTest {
        val testTrip = Trip(
            id = 1,
            title = "Test Trip",
            destination = "Test Dest",
            startDate = "2025-03-01T06:00:00Z",
            endDate = "2025-03-02T06:00:00Z",
            createdAt = "2025-02-28T12:00:00Z"
        )
        coEvery { mockTripDao.getTripById(1) } returns testTrip
        coEvery { mockTripDao.getTripById(99) } returns null

        val foundTrip = tripRepository.getTripById(1)
        val notFoundTrip = tripRepository.getTripById(99)

        assertEquals(testTrip, foundTrip)
        assertEquals(null, notFoundTrip)
        coVerify(exactly = 1) { mockTripDao.getTripById(1) }
        coVerify(exactly = 1) { mockTripDao.getTripById(99) }
    }

    @Test
    fun `insertTrip should call dao's insertTrip`() = runTest {
        val newTrip = Trip(
            title = "New Trip",
            destination = "New Dest",
            startDate = "2025-06-01T10:00:00Z",
            endDate = "2025-06-02T10:00:00Z",
            createdAt = "2025-05-30T15:00:00Z"
        )
        coEvery { mockTripDao.insertTrip(any()) } returns Unit

        tripRepository.insertTrip(newTrip)

        coVerify(exactly = 1) { mockTripDao.insertTrip(newTrip) }
    }

    @Test
    fun `deleteTrip should call dao's deleteTrip`() = runTest {
        val tripToDelete = Trip(
            id = 1,
            title = "Delete Me",
            destination = "Bye",
            startDate = "2025-07-01T10:00:00Z",
            endDate = "2025-07-02T10:00:00Z",
            createdAt = "2025-06-30T12:00:00Z"
        )
        coEvery { mockTripDao.deleteTrip(any()) } returns Unit

        tripRepository.deleteTrip(tripToDelete)

        coVerify(exactly = 1) { mockTripDao.deleteTrip(tripToDelete) }
    }

}