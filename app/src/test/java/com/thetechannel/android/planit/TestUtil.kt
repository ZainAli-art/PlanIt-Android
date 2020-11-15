package com.thetechannel.android.planit

import com.thetechannel.android.planit.data.source.domain.Category
import com.thetechannel.android.planit.data.source.domain.Task
import com.thetechannel.android.planit.data.source.domain.TaskDetail
import com.thetechannel.android.planit.data.source.domain.TaskMethod
import java.sql.Time

fun getTaskDetail(category: Category, method: TaskMethod, task: Task) = TaskDetail(
    id = task.id,
    categoryName = category.name,
    methodName = method.name,
    day = task.day,
    methodIconUrl = method.iconUrl,
    timeLapse = Time(method.workLapse.time + method.breakLapse.time),
    title = task.title,
    workStart = task.startAt,
    workEnd = Time(task.startAt.time + method.workLapse.time),
    breakStart = Time(task.startAt.time + method.workLapse.time),
    breakEnd = Time(task.startAt.time + method.workLapse.time + method.breakLapse.time),
    completed = task.completed
)