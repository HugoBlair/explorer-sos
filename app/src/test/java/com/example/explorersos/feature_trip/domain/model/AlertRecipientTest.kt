package com.example.explorersos.feature_trip.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ContactTest {

    @Test
    fun `valid email only - should create Contact`() {
        val recipient = Contact(
            recipientEmail = "test@example.com",
            recipientPhone = null
        )
        assertEquals("test@example.com", recipient.recipientEmail)
    }

    @Test
    fun `valid phone only - should create Contact`() {
        val recipient = Contact(
            recipientEmail = null,
            recipientPhone = "+19876543210"
        )
        assertEquals("+19876543210", recipient.recipientPhone)
    }

    @Test
    fun `both email and phone null - should throw exception`() {
        val exception = assertThrows(InvalidContactException::class.java) {
            Contact(null, null, null)
        }
        assertEquals("Either email or phone must be provided.", exception.message)
    }

    @Test
    fun `invalid email format - should throw exception`() {
        val exception = assertThrows(InvalidContactException::class.java) {
            Contact(1, "invalid-email", "6043880000")
        }
        assertEquals("Invalid email format.", exception.message)
    }

    @Test
    fun `invalid phone format - should throw exception`() {
        val exception = assertThrows(InvalidContactException::class.java) {
            Contact(1, "abc123@gmail.com", "111111111111111111111111111")
        }
        assertEquals("Invalid phone number format.", exception.message)
    }

    @Test
    fun `valid email and phone - should create Contact`() {
        val recipient = Contact(
            id = 1,
            recipientEmail = "user@example.com",
            recipientPhone = "+19876543210"
        )
        assertNotNull(recipient)
    }
}

