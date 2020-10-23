package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import com.thetechannel.android.planit.data.source.network.NetworkCategory
import com.thetechannel.android.planit.data.source.network.NetworkTask
import com.thetechannel.android.planit.data.source.network.NetworkTaskMethod
import java.sql.Timestamp

fun Category.toDataTransferObject() = NetworkCategory(
    id = id,
    name = name
)

fun Task.toDataTransferObject() = NetworkTask(
    id = id,
    day = day,
    startAt = Timestamp(startAt.time),
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)

fun TaskMethod.toDataTransferObject() = NetworkTaskMethod(
    id = id,
    name = name,
    workLapse = Timestamp(workLapse.time),
    breakLapse = Timestamp(breakLapse.time),
    iconUrl = iconUrl.toString()
)