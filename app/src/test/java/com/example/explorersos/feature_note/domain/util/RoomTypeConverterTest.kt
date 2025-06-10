package com.example.explorersos.feature_note.domain.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

// Mock ContactMethod enum for testing
enum class ContactMethod {
    EMAIL, PHONE, SMS
}

@DisplayName("RoomTypeConverters Tests")
class RoomTypeConvertersTest {

    private lateinit var typeConverters: RoomTypeConverters

    @BeforeEach
    fun setUp() {
        typeConverters = RoomTypeConverters()
    }

    @Nested
    @DisplayName("ContactMethod List Conversion Tests")
    inner class ContactMethodListTests {

        @Test
        @DisplayName("Should convert list to comma separated string")
        fun `toContactMethodList converts list to comma separated string`() {
            val contactMethods = listOf(ContactMethod.EMAIL, ContactMethod.PHONE, ContactMethod.SMS)
            val result = typeConverters.toContactMethodList(contactMethods)
            assertEquals("EMAIL,PHONE,SMS", result)
        }

        @Test
        @DisplayName("Should handle empty list")
        fun `toContactMethodList handles empty list`() {
            val emptyList = emptyList<ContactMethod>()
            val result = typeConverters.toContactMethodList(emptyList)
            assertEquals("", result)
        }

        @Test
        @DisplayName("Should handle single item list")
        fun `toContactMethodList handles single item list`() {
            val singleItem = listOf(ContactMethod.EMAIL)
            val result = typeConverters.toContactMethodList(singleItem)
            assertEquals("EMAIL", result)
        }

        @Test
        @DisplayName("Should convert comma separated string to list")
        fun `fromContactMethodList converts comma separated string to list`() {
            val input = "EMAIL,PHONE,SMS"
            val result = typeConverters.fromContactMethodList(input)
            val expected = listOf(ContactMethod.EMAIL, ContactMethod.PHONE, ContactMethod.SMS)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle empty string")
        fun `fromContactMethodList handles empty string`() {
            val result = typeConverters.fromContactMethodList("")
            assertEquals(emptyList<ContactMethod>(), result)
        }

        @Test
        @DisplayName("Should handle blank string")
        fun `fromContactMethodList handles blank string`() {
            val result = typeConverters.fromContactMethodList("   ")
            assertEquals(emptyList<ContactMethod>(), result)
        }

        @Test
        @DisplayName("Should handle single item")
        fun `fromContactMethodList handles single item`() {
            val result = typeConverters.fromContactMethodList("EMAIL")
            assertEquals(listOf(ContactMethod.EMAIL), result)
        }

        @Test
        @DisplayName("Should handle string with spaces")
        fun `fromContactMethodList handles string with spaces`() {
            val input = " EMAIL , PHONE , SMS "
            val result = typeConverters.fromContactMethodList(input)
            val expected = listOf(ContactMethod.EMAIL, ContactMethod.PHONE, ContactMethod.SMS)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Should throw exception for invalid enum value")
        fun `fromContactMethodList throws exception for invalid enum value`() {
            assertThrows<IllegalArgumentException> {
                typeConverters.fromContactMethodList("INVALID_METHOD")
            }
        }
    }

    @Nested
    @DisplayName("Int List Conversion Tests")
    inner class IntListTests {

        @Test
        @DisplayName("Should convert list to comma separated string")
        fun `toIntList converts list to comma separated string`() {
            val numbers = listOf(1, 2, 3, 4, 5)
            val result = typeConverters.toIntList(numbers)
            assertEquals("1,2,3,4,5", result)
        }

        @Test
        @DisplayName("Should handle empty list")
        fun `toIntList handles empty list`() {
            val emptyList = emptyList<Int>()
            val result = typeConverters.toIntList(emptyList)
            assertEquals("", result)
        }

        @Test
        @DisplayName("Should handle single item list")
        fun `toIntList handles single item list`() {
            val singleItem = listOf(42)
            val result = typeConverters.toIntList(singleItem)
            assertEquals("42", result)
        }

        @Test
        @DisplayName("Should handle negative numbers")
        fun `toIntList handles negative numbers`() {
            val numbers = listOf(-1, 0, 1)
            val result = typeConverters.toIntList(numbers)
            assertEquals("-1,0,1", result)
        }

        @Test
        @DisplayName("Should convert comma separated string to list")
        fun `fromIntList converts comma separated string to list`() {
            val input = "1,2,3,4,5"
            val result = typeConverters.fromIntList(input)
            val expected = listOf(1, 2, 3, 4, 5)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle empty string")
        fun `fromIntList handles empty string`() {
            val result = typeConverters.fromIntList("")
            assertEquals(emptyList<Int>(), result)
        }

        @Test
        @DisplayName("Should handle blank string")
        fun `fromIntList handles blank string`() {
            val result = typeConverters.fromIntList("   ")
            assertEquals(emptyList<Int>(), result)
        }

        @Test
        @DisplayName("Should handle single item")
        fun `fromIntList handles single item`() {
            val result = typeConverters.fromIntList("42")
            assertEquals(listOf(42), result)
        }

        @Test
        @DisplayName("Should handle string with spaces")
        fun `fromIntList handles string with spaces`() {
            val input = " 1 , 2 , 3 "
            val result = typeConverters.fromIntList(input)
            val expected = listOf(1, 2, 3)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle negative numbers")
        fun `fromIntList handles negative numbers`() {
            val input = "-1,0,1"
            val result = typeConverters.fromIntList(input)
            val expected = listOf(-1, 0, 1)
            assertEquals(expected, result)
        }

        @Test
        @DisplayName("Should throw exception for invalid number format")
        fun `fromIntList throws exception for invalid number format`() {
            assertThrows<NumberFormatException> {
                typeConverters.fromIntList("1,abc,3")
            }
        }
    }

    @Nested
    @DisplayName("Round-trip Conversion Tests")
    inner class RoundTripTests {

        @Test
        @DisplayName("ContactMethod round trip should maintain data integrity")
        fun `contactMethod round trip conversion maintains data integrity`() {
            val original = listOf(ContactMethod.EMAIL, ContactMethod.PHONE)
            val converted = typeConverters.toContactMethodList(original)
            val roundTrip = typeConverters.fromContactMethodList(converted)
            assertEquals(original, roundTrip)
        }

        @Test
        @DisplayName("Int list round trip should maintain data integrity")
        fun `int list round trip conversion maintains data integrity`() {
            val original = listOf(1, -5, 0, 100, 999)
            val converted = typeConverters.toIntList(original)
            val roundTrip = typeConverters.fromIntList(converted)
            assertEquals(original, roundTrip)
        }

        @Test
        @DisplayName("Empty lists should round trip correctly")
        fun `empty lists round trip correctly`() {
            val emptyContactMethods = emptyList<ContactMethod>()
            val emptyInts = emptyList<Int>()

            val contactMethodRoundTrip = typeConverters.fromContactMethodList(
                typeConverters.toContactMethodList(emptyContactMethods)
            )
            val intRoundTrip = typeConverters.fromIntList(
                typeConverters.toIntList(emptyInts)
            )

            assertEquals(emptyContactMethods, contactMethodRoundTrip)
            assertEquals(emptyInts, intRoundTrip)
        }
    }
}