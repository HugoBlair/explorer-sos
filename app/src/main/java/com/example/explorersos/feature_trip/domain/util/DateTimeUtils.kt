package com.example.explorersos.feature_trip.domain.util

import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle

object DateTimeUtils {

    fun getRelativeTimeString(utcString: String): String {
        try {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val parsedTime = OffsetDateTime.parse(utcString, formatter)


            val now = OffsetDateTime.now(ZoneOffset.UTC)
            val duration = Duration.between(parsedTime, now)


            return when {
                duration.toMinutes() < 1 -> "just now"
                duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
                duration.toHours() < 24 -> "${duration.toHours()} hours ago"
                duration.toDays() < 7 -> "${duration.toDays()} days ago"
                duration.toDays() < 365 -> "${duration.toDays() / 7} weeks ago"
                duration.toDays() >= 365 -> "${duration.toDays() / 365} years ago"
                else -> parsedTime.toLocalDate().toString() // fallback to date string
            }
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            return "Invalid date"
        }

    }

    fun getFormattedDisplayTime(eventInstant: Instant?): String {
        if (eventInstant == null) {
            return "Non Valid Date"
        }
        // Get the user's current timezone
        val userZoneId = ZoneId.systemDefault()

        // Create a formatter that produces a localized date and time
        // e.g., "Oct 27, 2023, 6:30:00 AM"
        val formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withZone(userZoneId)

        return formatter.format(eventInstant)
    }
}