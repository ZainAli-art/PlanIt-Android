package com.thetechannel.android.planit.data.source.network

import java.util.*

data class NetworkCategory(
    val id: Int = 1,
    val name: String = "Study"
)

data class NetworkTask(
    var id: String = UUID.randomUUID().toString(),
    var day: Long = System.currentTimeMillis(),
    var startAt: Long = System.currentTimeMillis(),
    var methodId: Int = 1,
    var title: String = "",
    var catId: Int = 1,
    var completed: Int = 0
)

data class NetworkTaskMethod(
    val id: Int = 1,
    val name: String = "pomodoro",
    val workLapse: Long = twentyFiveMins,
    val breakLapse: Long = fiveMins,
    val iconUrl: String = ""
)

private const val twentyFiveMins: Long = 25 * 60 * 1000
private const val fiveMins: Long = 5 * 60 * 1000