package com.thetechannel.android.planit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.*

@Entity(tableName = "days")
data class Day(
    @PrimaryKey @ColumnInfo(name = "date") var date: Date = Calendar.getInstance().time,
    @ColumnInfo(name = "start_at") var startAt: Time = Time(System.currentTimeMillis()),
    @ColumnInfo(name = "end_at") var endAt: Time = Time(System.currentTimeMillis())
)