package com.example.explorersos.feature_note.domain.util

import androidx.room.TypeConverter

class RoomTypeConverters {
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


