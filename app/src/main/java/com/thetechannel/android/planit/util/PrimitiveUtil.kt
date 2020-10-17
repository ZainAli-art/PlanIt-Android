package com.thetechannel.android.planit.util

import com.thetechannel.android.planit.data.source.domain.CategoryId
import com.thetechannel.android.planit.data.source.domain.TaskMethodId
import java.lang.IllegalArgumentException

fun Int.toCategoryId() = when(this) {
    1 -> CategoryId.STUDY
    2 -> CategoryId.BUSINESS
    3 -> CategoryId.SPORT
    4 -> CategoryId.HOBBY
    else -> throw IllegalArgumentException("Invalid id")
}

fun Int.toTaskMethodId() = when(this) {
    1 -> TaskMethodId.POMODORO
    2 -> TaskMethodId.EAT_THE_DEVIL
    else -> throw IllegalArgumentException("Invalid id")
}