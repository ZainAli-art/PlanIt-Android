package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.domain.Day
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskType
import com.thetechannel.android.planit.data.source.network.NetworkDay
import com.thetechannel.android.planit.data.source.network.NetworkTask
import com.thetechannel.android.planit.data.source.network.NetworkTaskType

fun Day.toDataTransferObject() = NetworkDay(
    date = date,
    startAt = startAt,
    endAt = endAt
)

fun Task.toDataTransferObject() = NetworkTask(
    id = id,
    day = day,
    startAt = startAt,
    typeId = typeId
)

fun TaskType.toDataTransferObject() = NetworkTaskType(
    id = id,
    name = name,
    workLapse = workLapse,
    breakLapse = breakLapse
)