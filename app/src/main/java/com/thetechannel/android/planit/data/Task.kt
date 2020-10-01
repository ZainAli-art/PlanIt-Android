package com.thetechannel.android.planit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "day") var day: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "start_at") var startAt: Time = Time(System.currentTimeMillis()),
    @ColumnInfo(name = "type_id") var typeId: Int = POMODORRO
)