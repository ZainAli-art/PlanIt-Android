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
import java.net.URI
import java.sql.Time
import java.sql.Timestamp

fun NetworkCategory.asDatabaseEntity() = DbCategory(
    id = id,
    name = name
)

fun NetworkCategory.asDomainModel() = Category(
    id = id,
    name = name
)

fun NetworkTaskMethod.asDatabaseEntity() = DbTaskMethod(
    id = id,
    name = name,
    workLapse = workLapse.time,
    breakLapse = breakLapse.time,
    iconUrl = iconUrl
)

fun NetworkTaskMethod.asDomainModel() = TaskMethod(
    id = id,
    name = name,
    workLapse =  Time(workLapse.time),
    breakLapse = Time(breakLapse.time),
    iconUrl = URI(iconUrl)
)

fun NetworkTask.asDatabaseEntity() = DbTask(
    id = id,
    day = day.time,
    startAt = startAt.time,
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)

fun NetworkTask.asDomainModel() = Task(
    id = id,
    day = day,
    startAt =  Time(startAt.time),
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)
