package com.example.ilinktrip.utils

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

class LocalDateTypeConverter {
    @TypeConverter
    fun fromLocalDate(value: LocalDate): String {
        return value.toString()
    }

    @TypeConverter
    fun dateToLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}
