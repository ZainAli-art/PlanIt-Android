package com.thetechannel.android.planit.data.source.network

import com.google.gson.annotations.SerializedName
import java.sql.Time
import java.util.*

data class NetworkCategory(
    @SerializedName("id") val id: Int = 1,
    @SerializedName("name") val name: String = "Study"
)

data class NetworkTask(
    @SerializedName("id") var id: String = UUID.randomUUID().toString(),
    @SerializedName("day") var day: Date = Calendar.getInstance().time,
    @SerializedName("start_at") var startAt: Time = Time(System.currentTimeMillis()),
    @SerializedName("method_id") var methodId: Int = 1,
    @SerializedName("title") var title: String = "",
    @SerializedName("cat_id") var catId: Int = 1,
    @SerializedName("completed") var completed: Boolean = false
)

data class NetworkTaskMethod(
    @SerializedName("id") val id: Int = 1,
    @SerializedName("name") val name: String = "pomodoro",
    @SerializedName("work_lapse") val workLapse: Time = Time(twentyFiveMins),
    @SerializedName("break_lapse") val breakLapse: Time = Time(fiveMins),
    @SerializedName("icon_url") val iconUrl: String = ""
)

private const val twentyFiveMins: Long = 25 * 60 * 1000
private const val fiveMins: Long = 5 * 60 * 1000