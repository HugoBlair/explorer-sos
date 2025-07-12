package com.example.explorersos.feature_trip.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AlertRecipientTest {

    @Test
    fun `valid email only - should create AlertRecipient`() {
        val recipient = AlertRecipient(
            recipientEmail = "test@example.com",
            recipientPhone = null
        )
        assertEquals("test@example.com", recipient.recipientEmail)
    }

    @Test
    fun `valid phone only - should create AlertRecipient`() {
        val recipient = AlertRecipient(
            recipientEmail = null,
            recipientPhone = "+19876543210"
        )
        assertEquals("+19876543210", recipient.recipientPhone)
    }

    @Test
    fun `both email and phone null - should throw exception`() {
        val exception = assertThrows(InvalidAlertRecipientException::class.java) {
            AlertRecipient(null, null, null)
        }
        assertEquals("Either email or phone must be provided.", exception.message)
    }

    @Test
    fun `invalid email format - should throw exception`() {
        val exception = assertThrows(InvalidAlertRecipientException::class.java) {
            AlertRecipient(1, "invalid-email", "6043880000")
        }
        assertEquals("Invalid email format.", exception.message)
    }

    @Test
    fun `invalid phone format - should throw exception`() {
        val exception = assertThrows(InvalidAlertRecipientException::class.java) {
            AlertRecipient(1, "abc123@gmail.com", "111111111111111111111111111")
        }
        assertEquals("Invalid phone number format.", exception.message)
    }

    @Test
    fun `valid email and phone - should create AlertRecipient`() {
        val recipient = AlertRecipient(
            id = 1,
            recipientEmail = "user@example.com",
            recipientPhone = "+19876543210"
        )
        assertNotNull(recipient)
    }
}

