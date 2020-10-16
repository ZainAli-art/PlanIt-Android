package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.database.DbCategory
import com.thetechannel.android.planit.data.source.database.DbDay
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskMethod
import com.thetechannel.android.planit.data.source.network.NetworkCategory
import com.thetechannel.android.planit.data.source.network.NetworkDay
import com.thetechannel.android.planit.data.source.network.NetworkTask
import com.thetechannel.android.planit.data.source.network.NetworkTaskMethod

fun NetworkCategory.toDatabaseEntity() = DbCategory(
    id = id,
    name = name
)

fun NetworkDay.toDatabaseEntity() = DbDay(
    date = date.time,
    startAt = startAt.time,
    endAt = endAt.time
)

fun NetworkTask.toDatabaseEntity() = DbTask(
    id = id,
    day = day.time,
    startAt = startAt.time,
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)

fun NetworkTaskMethod.toDatabaseEntity() = DbTaskMethod(
    id = id,
    name = name,
    workLapse = workLapse.time,
    breakLapse = breakLapse.time,
    iconUrl = iconUrl
)