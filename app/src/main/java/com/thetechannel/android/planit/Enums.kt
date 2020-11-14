package com.thetechannel.android.planit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class TaskFilterType : Parcelable {
    ALL,
    PENDING,
    COMPLETED,
    COMPLETED_TODAY
}