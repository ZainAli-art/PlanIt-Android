package com.thetechannel.android.planit.data.source.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class DbCategory(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String
)

@Entity(tableName = "tasks")
data class DbTask(
    @PrimaryKey @ColumnInfo(name = "id")  var id: String,
    @ColumnInfo(name = "day")  var day: Long,
    @ColumnInfo(name = "start_at")  var startAt: Long,
    @ColumnInfo(name = "method_id")  var methodId: Int,
    @ColumnInfo(name = "title")  var title: String,
    @ColumnInfo(name = "cat_id")  var catId: Int,
    @ColumnInfo(name = "completed")  var completed: Boolean
)

@Entity(tableName = "task_methods")
data class DbTaskMethod(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "work_lapse") val workLapse: Long,
    @ColumnInfo(name = "break_lapse") val breakLapse: Long,
    @ColumnInfo(name = "icon_url") val iconUrl: String
)

data class DbTaskDetail(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "method") val method: String,
    @ColumnInfo(name = "day") val day: Long,
    @ColumnInfo(name = "method_icon_url") val methodIconUrl: String,
    @ColumnInfo(name = "time_lapse") val timeLapse: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "work_start") val workStart: Long,
    @ColumnInfo(name = "work_end") val workEnd: Long,
    @ColumnInfo(name = "break_start") val breakStart: Long,
    @ColumnInfo(name = "break_end") val breakEnd: Long,
    @ColumnInfo(name = "completed") val completed: Boolean
)