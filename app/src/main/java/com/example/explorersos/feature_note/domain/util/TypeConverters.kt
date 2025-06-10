package com.example.explorersos.feature_note.domain.util

import androidx.room.TypeConverter

class TypeConverters {
    @TypeConverter
    fun toContactMethodList(value: List<ContactMethod>): String {
        return value.joinToString(",") { it.name }
    }

    @TypeConverter
    fun fromContactMethodList(value: String): List<ContactMethod> {
        return value.split(",").map { ContactMethod.valueOf(it) }

    }

    @TypeConverter
    fun toIntList(value: List<Int>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun fromIntList(value: String): List<Int> {
        return value.split(",").map { it.toInt() }
    }
}