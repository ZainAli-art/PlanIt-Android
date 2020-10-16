package com.thetechannel.android.planit.data.source.network

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*

const val POMODORRO = 1

data class NetworkCategory(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class NetworkDay(
    @SerializedName("date") val date: Date = Calendar.getInstance().time,
    @SerializedName("start_at") val startAt: Time = Time(System.currentTimeMillis()),
    @SerializedName("end_at") val endAt: Time = Time(System.currentTimeMillis())
)

data class NetworkTask(
    @SerializedName("id") var id: String = UUID.randomUUID().toString(),
    @SerializedName("day") var day: Date = Calendar.getInstance().time,
    @SerializedName("start_at") var startAt: Time = Time(System.currentTimeMillis()),
    @SerializedName("method_id") var methodId: Int = POMODORRO,
    @SerializedName("title") var title: String = "",
    @SerializedName("cat_id") var catId: Int = 1,
    @SerializedName("completed") var completed: Boolean = false
)

data class NetworkTaskMethod(
    @SerializedName("id") val id: Int = POMODORRO,
    @SerializedName("name") val name: String = "pomodorro",
    @SerializedName("work_lapse") val workLapse: Time = Time(twentyFiveMins),
    @SerializedName("break_lapse") val breakLapse: Time = Time(fiveMins),
    @SerializedName("icon_url") val iconUrl: String = ""
)

private const val twentyFiveMins: Long = 25 * 60 * 1000
private const val fiveMins: Long = 5 * 60 * 1000