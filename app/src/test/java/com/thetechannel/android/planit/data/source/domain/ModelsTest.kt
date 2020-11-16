package com.thetechannel.android.planit.data.source.domain

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import java.net.URI
import java.sql.Time
import java.util.*

class ModelsTest {
    @Test
    fun givenTaskDetail_getInterval_returnsIntervalString() {
        val workStart = Time(1605431549682L)
        val workEnd = Time(workStart.time + 25 * 60000)
        val breakStart = workEnd
        val breakEnd = Time(workEnd.time + 5 * 60000)
        val timeLapse = Time(breakEnd.time - workStart.time)
        val detail = TaskDetail(
            "detail_1",
            "Study",
            "Pomodoro",
            Calendar.getInstance().time,
            URI("https://localhost"),
            timeLapse,
            "Maths Assignment",
            workStart,
            workEnd,
            breakStart,
            breakEnd,
            false
        )

        MatcherAssert.assertThat(detail.interval(), CoreMatchers.`is`("02:12 PM - 02:42 PM"))
    }

    @Test
    fun givenTaskDetail_getTimeRequiredForTask_returnsRequiredTimeString() {
        val workStart = Time(System.currentTimeMillis())
        val workEnd = Time(workStart.time + 25 * 60000)
        val breakStart = workEnd
        val breakEnd = Time(workEnd.time + 5 * 60000)
        val timeLapse = Time(breakEnd.time - workStart.time)
        val detail = TaskDetail(
            "detail_1",
            "Study",
            "Pomodoro",
            Calendar.getInstance().time,
            URI("https://localhost"),
            timeLapse,
            "Maths Assignment",
            workStart,
            workEnd,
            breakStart,
            breakEnd,
            false
        )

        val interval: String = detail.timeRequired()

        MatcherAssert.assertThat(interval, CoreMatchers.`is`("30 min"))
    }
}