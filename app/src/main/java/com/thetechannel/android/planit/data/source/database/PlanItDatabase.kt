package com.thetechannel.android.planit.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DbDay::class, DbTaskType::class, DbTask::class],
    version = 1, exportSchema = false)
abstract class PlanItDatabase : RoomDatabase() {
    abstract fun daysDao(): DaysDao
    abstract fun taskTypesDao(): TaskTypesDao
    abstract fun tasksDao(): TasksDao
}