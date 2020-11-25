package com.thetechannel.android.planit.util

import java.util.*

fun Date.isToday() = isSameDay(Calendar.getInstance().time)

fun Date.isSameDay(that: Date): Boolean {
    val c1 = Calendar.getInstance()
    val c2 = Calendar.getInstance()

    c1.time = this
    c2.time = that

    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
}

fun Int.toBoolean() = this != 0

fun Boolean.toInt() = if (this) 1 else 0