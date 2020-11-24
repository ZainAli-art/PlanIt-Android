package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.database.DbCategory
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskMethod
import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
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
    workLapse = Timestamp(workLapse.time),
    breakLapse = Timestamp(breakLapse.time),
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
    day = Timestamp(day.time),
    startAt = Timestamp(startAt.time),
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
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
