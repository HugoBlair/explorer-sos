package com.example.explorersos.feature_trip.domain.model

import com.example.explorersos.feature_trip.domain.util.ContactMethod
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class AlertTest {

    @Test
    fun `valid alert - should create Alert`() {
        val alert = Alert(
            alertMessage = "Test alert",
            alertHoursAfter = 24,
            alertInstructions = "Follow these steps",
            alertOrder = listOf(ContactMethod.EMAIL),
            recipientsId = listOf(1, 2)
        )
        assertNotNull(alert)
        assertEquals("Test alert", alert.alertMessage)
    }

    @Test
    fun `blank alert message - should throw exception`() {
        val exception = assertThrows(Alert.InvalidAlertException::class.java) {
            Alert(
                alertMessage = "",
                alertInstructions = "Instructions",
                alertOrder = listOf(ContactMethod.PHONE),
                recipientsId = listOf(1)
            )
        }
        assertEquals("Alert message cannot be blank.", exception.message)
    }

    @Test
    fun `empty alert instructions - should throw exception`() {
        val exception = assertThrows(Alert.InvalidAlertException::class.java) {
            Alert(
                alertMessage = "Message",
                alertInstructions = "",
                alertOrder = listOf(ContactMethod.EMAIL),
                recipientsId = listOf(1)
            )
        }
        assertEquals("Alert instructions cannot be blank.", exception.message)
    }

    @Test
    fun `empty contact methods - should throw exception`() {
        val exception = assertThrows(Alert.InvalidAlertException::class.java) {
            Alert(
                alertMessage = "Message",
                alertInstructions = "Instructions",
                alertOrder = emptyList(),
                recipientsId = listOf(1)
            )
        }
        assertEquals("At least one contact method must be provided.", exception.message)
    }

    @Test
    fun `empty recipients list - should throw exception`() {
        val exception = assertThrows(Alert.InvalidAlertException::class.java) {
            Alert(
                alertMessage = "Message",
                alertInstructions = "Instructions",
                alertOrder = listOf(ContactMethod.SMS),
                recipientsId = emptyList()
            )
        }
        assertEquals("At least one recipient ID must be provided.", exception.message)
    }

    @Test
    fun `non-positive alert time - should throw exception`() {
        val exception = assertThrows(Alert.InvalidAlertException::class.java) {
            Alert(
                alertMessage = "Message",
                alertHoursAfter = 0,
                alertInstructions = "Instructions",
                alertOrder = listOf(ContactMethod.SMS),
                recipientsId = listOf(1)
            )
        }
        assertEquals("Alert time must be greater than zero.", exception.message)
    }

}