package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.database.DbDay
import com.thetechannel.android.planit.data.source.database.DbTask
import com.thetechannel.android.planit.data.source.database.DbTaskDetail
import com.thetechannel.android.planit.data.source.database.DbTaskType
import com.thetechannel.android.planit.data.source.domain.Day
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskType
import java.sql.Time
import java.util.*

fun DbDay.toDomainModel() = Day(
    date = Date(date),
    startAt = Time(startAt),
    endAt = Time(endAt)
)

fun DbTask.toDomainModel() = Task(
    id = id,
    day = Date(day),
    startAt = Time(startAt),
    typeId = typeId
)

fun DbTaskType.toDomainModel() = TaskType(
    id = id,
    name = name,
    workLapse = Time(workLapse),
    breakLapse = Time(breakLapse)
)

fun DbTaskDetail.toDomainModel() = TaskDetail(
    id = id,
    name = name,
    workStart = Time(workStart),
    workEnd = Time(workEnd),
    breakStart = Time(breakStart),
    breakEnd = Time(breakEnd)
)