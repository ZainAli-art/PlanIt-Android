package com.thetechannel.android.planit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity(tableName = "task_types")
data class TaskType(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = POMODORRO,
    @ColumnInfo(name = "name") var name: String = "pomodorro",
    @ColumnInfo(name = "work_lapse") var workLapse: Time = Time(twentyFiveMins),
    @ColumnInfo(name = "break_lapse") var breakLapse: Time = Time(fiveMins)
)

const val POMODORRO = 1

private const val twentyFiveMins: Long = 25 * 60 * 1000
private const val fiveMins: Long = 5 * 60 * 1000