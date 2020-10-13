package com.thetechannel.android.planit.data.source.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days")
data class DbDay(
    @PrimaryKey @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "start_at") val startAt: Long,
    @ColumnInfo(name = "end_at") val endAt: Long
)

@Entity(tableName = "tasks")
data class DbTask(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "day") val day: Long,
    @ColumnInfo(name = "start_at") val startAt: Long,
    @ColumnInfo(name = "type_id") val typeId: Int
)

@Entity(tableName = "task_types")
data class DbTaskType(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "work_lapse") val workLapse: Long,
    @ColumnInfo(name = "break_lapse") val breakLapse: Long
)

data class DbTaskDetail(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "work_start") val workStart: Long,
    @ColumnInfo(name = "work_end") val workEnd: Long,
    @ColumnInfo(name = "break_start") val breakStart: Long,
    @ColumnInfo(name = "break_end") val breakEnd: Long
)