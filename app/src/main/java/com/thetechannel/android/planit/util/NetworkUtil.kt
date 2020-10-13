package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.database.DbDay
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskType
import com.thetechannel.android.planit.data.source.network.NetworkDay
import com.thetechannel.android.planit.data.source.network.NetworkTask
import com.thetechannel.android.planit.data.source.network.NetworkTaskType

fun NetworkDay.toDatabaseEntity() = DbDay(
    date = date.time,
    startAt = startAt.time,
    endAt = endAt.time
)

fun NetworkTask.toDatabaseEntity() = DbTask(
    id = id,
    day = day.time,
    startAt = startAt.time,
    typeId = typeId
)

fun NetworkTaskType.toDatabaseEntity() = DbTaskType(
    id = id,
    name = name,
    workLapse = workLapse.time,
    breakLapse = breakLapse.time
)