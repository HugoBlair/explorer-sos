package com.example.explorersos.feature_note.domain.util

import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    fun getRelativeTimeString(utcString: String): String {
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
    }

    fun formatDateTime(dateTime: String, pattern: String = "MMM dd, yyyy"): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern(pattern)
        val parsedTime = OffsetDateTime.parse(dateTime, formatter)
        return parsedTime.format(outputFormatter)
    }
}