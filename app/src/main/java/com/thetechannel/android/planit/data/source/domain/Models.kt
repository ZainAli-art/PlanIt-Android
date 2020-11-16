package com.thetechannel.android.planit.data.source.domain

import java.lang.StringBuilder
import java.net.URI
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class Category(
    var id: Int,
    var name: String
)

data class Task(
    var id: String,
    var day: Date,
    var startAt: Time,
    var methodId: Int,
    var title: String,
    var catId: Int,
    var completed: Boolean
)

data class TaskMethod(
    var id: Int,
    var name: String,
    var workLapse: Time,
    var breakLapse: Time,
    var iconUrl: URI
)

data class TaskDetail(
    val id: String,
    val categoryName: String,
    val methodName: String,
    val day: Date,
    val methodIconUrl: URI,
    val timeLapse: Time,
    val title: String,
    val workStart: Time,
    val workEnd: Time,
    val breakStart: Time,
    val breakEnd: Time,
    val completed: Boolean
) {
    fun interval(): String {
        val formatter = SimpleDateFormat("hh:mm a")
        return "${formatter.format(workStart)} - ${formatter.format(breakEnd)}"
    }

    fun timeRequired(): String {
        val sb = StringBuilder()
        val hours: Long = TimeUnit.MILLISECONDS.toHours(timeLapse.time)
        if (hours > 0L) sb.append("$hours hrs")
        val mins: Long = TimeUnit.MILLISECONDS.toMinutes(timeLapse.time)
        if (mins > 0L) sb.append("$mins min")

        return sb.toString()
    }
}