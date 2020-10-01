package com.thetechannel.android.planit.util

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import java.sql.Time
import java.util.*

class ConvertersTest {
    private val converters = Converters()

    @Test
    fun fromDate_givenDate_returnsInterchangeableTimestamp() {
        // Given - A Date
        val date: Date = Calendar.getInstance().time

        // When - Passed to converter
        val millis = converters.fromDate(date)

        // Then - Gives a timestamp which is tranferrable to the same Date
        assertThat(millis, `is`(notNullValue()))
        val loadedDate = converters.toDate(millis)
        assertThat(loadedDate, `is`(date))
    }

    @Test
    fun fromTime_givenTime_returnsInterchangeableTimeStamp() {
        val time = Time(System.currentTimeMillis())

        val millis = converters.fromTime(time)

        assertThat(millis, `is`(notNullValue()))
        val loadedTime = converters.toTime(millis)
        assertThat(loadedTime, `is`(time))
    }
}