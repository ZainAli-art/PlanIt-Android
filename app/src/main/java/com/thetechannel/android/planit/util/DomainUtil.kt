package com.thetechannel.android.planit.util

import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.source.database.DbCategory
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskMethod
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.source.network.NetworkCategory
import com.thetechannel.android.planit.data.source.network.NetworkTask
import com.thetechannel.android.planit.data.source.network.NetworkTaskMethod
import java.sql.Time
import java.sql.Timestamp


fun Category.asDataTransferObject() = NetworkCategory(
    id = id,
    name = name
)

fun Category.asDatabaseEntity() = DbCategory(
    id = id,
    name = name
)

fun TaskMethod.asDataTransferObject() = NetworkTaskMethod(
    id = id,
    name = name,
    workLapse = workLapse.time,
    breakLapse = breakLapse.time,
    iconUrl = iconUrl.toString()
)

fun TaskMethod.asDatabaseEntity() = DbTaskMethod(
    id = id,
    name = name,
    workLapse = workLapse.time,
    breakLapse = breakLapse.time,
    iconUrl = iconUrl.toString()
)

fun Task.asDataTransferObject() = NetworkTask(
    id = id,
    day = day.time,
    startAt = startAt.time,
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed.toInt()
)

fun Task.asDatabaseEntity() = DbTask(
    id = id,
    day = day.time,
    startAt = startAt.time,
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)

internal fun getPieEntries(details: List<TaskDetail>): List<PieEntry> {
    val entries = mutableListOf<PieEntry>()
    val entryMap = mutableMapOf<String, Int>()
    for (d in details) {
        val count: Int = entryMap[d.categoryName] ?: 0
        entryMap[d.categoryName] = count + 1
    }
    for (e in entryMap) {
        entries.add(PieEntry(e.value.toFloat(), e.key))
    }
    return entries
}
