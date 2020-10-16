package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.database.*
import com.thetechannel.android.planit.data.source.domain.*
import java.net.URI
import java.sql.Time
import java.util.*

fun DbCategory.toDomainModel() = Category(
    id = id,
    name = name
)

fun DbDay.toDomainModel() = Day(
    date = Date(date),
    startAt = Time(startAt),
    endAt = Time(endAt)
)

fun DbTask.toDomainModel() = Task(
    id = id,
    day = Date(day),
    startAt = Time(startAt),
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)

fun DbTaskMethod.toDomainModel() = TaskMethod(
    id = id,
    name = name,
    workLapse = Time(workLapse),
    breakLapse = Time(breakLapse),
    iconUrl = URI(iconUrl)
)

fun DbTaskDetail.toDomainModel() = TaskDetail(
    id,
    category,
    method,
    methodIconUrl = URI(methodIconUrl),
    timeLapse =  Time(timeLapse),
    title = title,
    workStart = Time(workStart),
    workEnd = Time(workEnd),
    breakStart = Time(breakStart),
    breakEnd = Time(breakEnd)
)