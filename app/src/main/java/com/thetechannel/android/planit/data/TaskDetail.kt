package com.thetechannel.android.planit.data

import androidx.room.ColumnInfo
import java.sql.Time

data class TaskDetail(
    var id: String,
    var name: String,
    @ColumnInfo(name = "work_start") var workStart: Time,
    @ColumnInfo(name = "work_end") var workEnd: Time,
    @ColumnInfo(name = "break_start") var breakStart: Time,
    @ColumnInfo(name = "break_end") var breakEnd: Time
)