package com.example.explorersos.feature_trip.domain.util

import androidx.room.TypeConverter
import java.time.Instant

class RoomTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun dateToTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun toContactMethodList(value: List<ContactMethod>): String {
        return value.joinToString(",") { it.name }
    }

    @TypeConverter
    fun fromContactMethodList(value: String): List<ContactMethod> {
        if (value.isBlank()) return emptyList()
        return value.split(",").map { ContactMethod.valueOf(it.trim()) }
    }

    @TypeConverter
    fun toIntList(value: List<Int>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun fromIntList(value: String): List<Int> {
        if (value.isBlank()) return emptyList()
        return value.split(",").map { it.trim().toInt() }
    }
}


