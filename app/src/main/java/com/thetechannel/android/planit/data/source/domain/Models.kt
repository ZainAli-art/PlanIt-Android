package com.thetechannel.android.planit.data.source.domain

import java.sql.Time
import java.util.*

data class Day(
    var date: Date,
    var startAt: Time,
    var endAt: Time
)

data class Task(
    var id: String,
    var day: Date,
    var startAt: Time,
    var typeId: Int
)

data class TaskType(
    var id: Int,
    var name: String,
    var workLapse: Time,
    var breakLapse: Time
)

data class TaskDetail(
    val id: String,
    val name: String,
    val workStart: Time,
    val workEnd: Time,
    val breakStart: Time,
    val breakEnd: Time
)