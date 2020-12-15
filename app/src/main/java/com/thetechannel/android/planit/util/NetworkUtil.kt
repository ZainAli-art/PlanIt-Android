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
import java.sql.Date
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
    workLapse = workLapse,
    breakLapse = breakLapse,
    iconUrl = iconUrl
)

fun NetworkTaskMethod.asDomainModel() = TaskMethod(
    id = id,
    name = name,
    workLapse =  Time(workLapse),
    breakLapse = Time(breakLapse),
    iconUrl = URI(iconUrl)
)

fun NetworkTask.asDatabaseEntity() = DbTask(
    id = id,
    day = day,
    startAt = startAt,
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed.toBoolean()
)

fun NetworkTask.asDomainModel() = Task(
    id = id,
    day = Date(day),
    startAt =  Time(startAt),
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed.toBoolean()
)
