package com.thetechannel.android.planit.util

import com.github.mikephil.charting.data.PieEntry
import com.thetechannel.android.planit.data.source.database.*
import com.thetechannel.android.planit.data.source.domain.*
import java.net.URI
import java.sql.Time
import java.util.*

fun DbCategory.asDomainModel() = Category(
    id = id,
    name = name
)

fun DbTaskMethod.asDomainModel() = TaskMethod(
    id = id,
    name = name,
    workLapse = Time(workLapse),
    breakLapse = Time(breakLapse),
    iconUrl = URI(iconUrl)
)

fun DbTask.asDomainModel() = Task(
    id = id,
    day = Date(day),
    startAt = Time(startAt),
    methodId = methodId,
    title = title,
    catId = catId,
    completed = completed
)

fun DbTaskDetail.asDomainModel() = TaskDetail(
    id,
    category,
    method,
    day = Date(day),
    methodIconUrl = URI(methodIconUrl),
    timeLapse =  Time(timeLapse),
    title = title,
    workStart = Time(workStart),
    workEnd = Time(workEnd),
    breakStart = Time(breakStart),
    breakEnd = Time(breakEnd),
    completed = completed
)

fun TodayPieDataView.asPieEntry() = PieEntry(count.toFloat(), name)
