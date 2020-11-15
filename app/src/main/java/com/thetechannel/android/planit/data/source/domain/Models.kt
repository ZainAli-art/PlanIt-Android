package com.thetechannel.android.planit.data.source.domain

import java.net.URI
import java.sql.Time
import java.util.*

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
)