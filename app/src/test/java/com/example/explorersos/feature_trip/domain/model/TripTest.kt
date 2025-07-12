package com.example.explorersos.feature_trip.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TripTest {

    @Test
    fun testTripCreationWithAllProperties() {
        val trip = Trip(
            title = "Tararua Tramp",
            destination = "Tararua Ranges",
            startDate = "2024-12-12T00:00:00Z",
            endDate = "2026-12-12T00:00:00Z",
            description = "A trip to Tararua Ranges",
            createdAt = "2023-11-30T12:34:56Z",
            isActive = true,
            alertId = 123456789
        )

        assertEquals("Tararua Tramp", trip.title)
        assertEquals("Tararua Ranges", trip.destination)
        assertEquals("2024-12-12T00:00:00Z", trip.startDate)
        assertEquals("2026-12-12T00:00:00Z", trip.endDate)
        assertEquals("A trip to Tararua Ranges", trip.description)
        assertEquals("2023-11-30T12:34:56Z", trip.createdAt)
        assertTrue(trip.isActive)
        assertEquals(123456789, trip.alertId)
    }

    @Test
    fun testTripCreationWithDefaultValues() {
        val trip = Trip(
            title = "Ridge Traverse",
            destination = "Wainui Hill",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z"
        )

        assertEquals("Ridge Traverse", trip.title)
        assertEquals("Wainui Hill", trip.destination)
        assertEquals(
            "I am planning the \"Ridge Traverse\" trip to Wainui Hill. I am planning to leave on 2025-01-01T00:00:00Z and return on 2025-01-02T00:00:00Z.",
            trip.description
        )
        assertFalse(trip.isActive)
        assertNull(trip.alertId)
    }

    @Test
    fun testTripCreationWithActiveTrip() {
        val trip = Trip(
            title = "Ridge Traverse",
            destination = "Wainui Hill",
            startDate = "2025-01-01T00:00:00Z",
            endDate = "2025-01-02T00:00:00Z",
            createdAt = "2024-12-31T23:59:59Z",
            isActive = true
        )

        assertEquals("Ridge Traverse", trip.title)
        assertEquals("Wainui Hill", trip.destination)
        assertEquals(
            "I am currently on the \"Ridge Traverse\" trip to Wainui Hill. I am planning to leave on 2025-01-01T00:00:00Z and return on 2025-01-02T00:00:00Z.",
            trip.description
        )
        assertTrue(trip.isActive)
        assertNull(trip.alertId)
    }

    @Test
    fun `test trip creation with blank title throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "",
                destination = "Tararua Ranges",
                startDate = "2024-12-12T00:00:00Z",
                endDate = "2026-12-12T00:00:00Z",
                createdAt = "2023-11-30T12:34:56Z"
            )
        }
        assertEquals("Title cannot be blank.", exception.message)
    }

    @Test
    fun `test trip creation with blank destination throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "  ",
                startDate = "2024-12-12T00:00:00Z",
                endDate = "2026-12-12T00:00:00Z",
                createdAt = "2023-11-30T12:34:56Z"
            )
        }
        assertEquals("Destination cannot be blank.", exception.message)
    }

    @Test
    fun `test trip creation with blank start date throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "Valid Destination",
                startDate = "  ",
                endDate = "2026-12-12T00:00:00Z",
                createdAt = "2023-11-30T12:34:56Z"
            )
        }
        assertEquals("Start date cannot be blank.", exception.message)
    }

    @Test
    fun `test trip creation with blank end date throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "Valid Destination",
                startDate = "2024-12-12T00:00:00Z",
                endDate = "  ",
                createdAt = "2023-11-30T12:34:56Z"
            )
        }
        assertEquals("End date cannot be blank.", exception.message)
    }

    @Test
    fun `test trip creation with start date after end date throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "Valid Destination",
                startDate = "2025-12-12T00:00:00Z",
                endDate = "2024-12-12T00:00:00Z",
                createdAt = "2023-11-30T12:34:56Z"
            )
        }
        assertEquals("Start date cannot be after end date.", exception.message)
    }

    @Test
    fun `test trip creation with created at in the future throws InvalidTripException`() {
        val futureTime =
            java.time.OffsetDateTime.now(java.time.ZoneOffset.UTC).plusSeconds(1000).toString()
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "Valid Destination",
                startDate = "2024-12-12T00:00:00Z",
                endDate = "2026-12-12T00:00:00Z",
                createdAt = futureTime
            )
        }
        assertEquals("Created at cannot be in the future.", exception.message)
    }

    @Test
    fun `test trip creation with negative alert ID throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "Valid Destination",
                startDate = "2024-12-12T00:00:00Z",
                endDate = "2026-12-12T00:00:00Z",
                alertId = -1
            )
        }
        assertEquals("Alert ID cannot be negative.", exception.message)
    }
}
