package com.thetechannel.android.planit.util

import androidx.room.TypeConverter
import java.sql.Time
import java.util.*

class Converters {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTime(time: Time?): Long? {
        return time?.time
    }

    @TypeConverter
    fun toDate(millis: Long?): Date? {
        return millis?.let { Date(it) }
    }

    @TypeConverter
    fun toTime(millis: Long?): Time? {
        return millis?.let { Time(it) }
    }
}