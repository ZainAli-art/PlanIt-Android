package com.thetechannel.android.planit.notification

import android.app.Notification
import android.content.Context

interface NotificationFactory {
    val context: Context

    fun create(name: String): Notification
}

interface NotificationIdFactory {
    fun create(): Int
}