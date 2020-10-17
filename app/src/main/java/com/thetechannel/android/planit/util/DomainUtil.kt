package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Day
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.source.network.NetworkCategory
import com.thetechannel.android.planit.data.source.network.NetworkDay
import com.thetechannel.android.planit.data.source.network.NetworkTask
import com.thetechannel.android.planit.data.source.network.NetworkTaskMethod

fun Category.toDataTransferObject() = NetworkCategory(
    id = id.data,
    name = name
)

fun Day.toDataTransferObject() = NetworkDay(
    date = date,
    startAt = startAt,
    endAt = endAt
)

fun Task.toDataTransferObject() = NetworkTask(
    id = id,
    day = day,
    startAt = startAt,
    methodId = methodId,
    title = title,
    catId = catId
)

fun TaskMethod.toDataTransferObject() = NetworkTaskMethod(
    id = id.data,
    name = name,
    workLapse = workLapse,
    breakLapse = breakLapse,
    iconUrl = iconUrl.toString()
)