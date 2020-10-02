package com.thetechannel.android.planit.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thetechannel.android.planit.data.Day
import com.thetechannel.android.planit.data.Task
import com.thetechannel.android.planit.data.TaskDetail
import com.thetechannel.android.planit.data.TaskType
import com.thetechannel.android.planit.util.Converters

@Database(
    entities = arrayOf(Day::class, TaskType::class, Task::class),
    version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PlanItDatabase : RoomDatabase() {
    abstract fun daysDao(): DaysDao
    abstract fun taskTypesDao(): TaskTypesDao
    abstract fun tasksDao(): TasksDao
}