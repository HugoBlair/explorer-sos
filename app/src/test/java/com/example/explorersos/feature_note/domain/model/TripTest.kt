package com.example.explorersos.feature_note.domain.model

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class TripTest {


    @Test
    fun testTripCreationWithAllProperties() {
        val trip = Trip(
            title = "Tararua Tramp",
            destination = "Tararua Ranges",
            startDate = "12/12/2024",
            endDate = "12/12/2026",
            description = "A trip to Tararua Ranges",
            createdAt = 123456789,
            isActive = true,
            alertId = 123456789
        )

        assertEquals("Tararua Tramp", trip.title)
        assertEquals("Tararua Ranges", trip.destination)
        assertEquals("12/12/2024", trip.startDate)
        assertEquals("12/12/2026", trip.endDate)
        assertEquals("A trip to Tararua Ranges", trip.description)
        assertEquals(123456789, trip.createdAt)
        assertTrue(trip.isActive)
        assertEquals(123456789, trip.alertId)
    }

    @Test
    fun testTripCreationWithDefaultValues() {
        val trip = Trip(
            title = "Ridge Traverse",
            destination = "Wainui Hill",
            startDate = "01/01/2025",
            endDate = "02/01/2025",
            createdAt = System.currentTimeMillis()
        )

        assertEquals("Ridge Traverse", trip.title)
        assertEquals("Wainui Hill", trip.destination)
        assertEquals(
            "I am planning the \"Ridge Traverse\" trip to Wainui Hill. I am planning to leave on 01/01/2025" +
                    " and return on 02/01/2025.", trip.description
        ) // Default value for trip description
        assertFalse(trip.isActive)   // Default value for isActive
        assertNull(trip.alertId)    // Default value for nullable Long
    }

    @Test
    fun testTripCreationWithActiveTrip() {
        val trip = Trip(
            title = "Ridge Traverse",
            destination = "Wainui Hill",
            startDate = "01/01/2025",
            endDate = "02/01/2025",
            createdAt = System.currentTimeMillis(),
            isActive = true
        )

        assertEquals("Ridge Traverse", trip.title)
        assertEquals("Wainui Hill", trip.destination)
        assertEquals(
            "I am currently on the \"Ridge Traverse\" trip to Wainui Hill. I am planning to leave on 01/01/2025" +
                    " and return on 02/01/2025.", trip.description
        ) // Default value for trip description
        assertTrue(trip.isActive)   // Default value for isActive
        assertNull(trip.alertId)    // Default value for nullable Long
    }


    @Test
    fun `test trip creation with blank title throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "", // Blank title
                destination = "Tararua Ranges",
                startDate = "12/12/2024",
                endDate = "12/12/2026",
                createdAt = 123456789L
            )
        }
        assertEquals("Title cannot be blank.", exception.message)
    }

    @Test
    fun `test trip creation with blank destination throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "  ", // Blank destination
                startDate = "12/12/2024",
                endDate = "12/12/2026",
                createdAt = 123456789L
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
                startDate = "  ", // Blank start date
                endDate = "12/12/2026",
                createdAt = 123456789L
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
                startDate = "12/12/2024",
                endDate = "  ", // Blank end date
                createdAt = 123456789
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
                startDate = "12/12/2025",
                endDate = "12/12/2024", // Blank end date
                createdAt = 123456789
            )
        }
        assertEquals("Start date cannot be after end date.", exception.message)
    }

    @Test
    fun `test trip creation with created at in the future throws InvalidTripException`() {
        val exception = assertThrows<Trip.InvalidTripException> {
            Trip(
                title = "Valid Title",
                destination = "Valid Destination",
                startDate = "12/12/2024",
                endDate = "12/12/2026",
                createdAt = System.currentTimeMillis() + 1000 // Created at in the future
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
                startDate = "12/12/2024",
                endDate = "12/12/2026",
                alertId = -1 // Negative alert ID
            )
        }
        assertEquals("Alert ID cannot be negative.", exception.message)
    }


}




