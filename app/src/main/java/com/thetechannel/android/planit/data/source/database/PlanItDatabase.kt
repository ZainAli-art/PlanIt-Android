package com.thetechannel.android.planit.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DbDay::class, DbCategory::class, DbTaskMethod::class, DbTask::class],
    version = 1, exportSchema = false)
abstract class PlanItDatabase : RoomDatabase() {
    abstract val daysDao: DaysDao
    abstract val taskMethodsDao: TaskMethodsDao
    abstract val tasksDao: TasksDao
    abstract val categoriesDao: CategoriesDao
}