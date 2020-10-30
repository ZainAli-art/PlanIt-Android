package com.thetechannel.android.planit.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DbCategory::class, DbTaskMethod::class, DbTask::class],
    views = [TasksOverView::class, TodayPieDataView::class],
    version = 1, exportSchema = false)
abstract class PlanItDatabase : RoomDatabase() {
    abstract val taskMethodsDao: TaskMethodsDao
    abstract val tasksDao: TasksDao
    abstract val categoriesDao: CategoriesDao
}