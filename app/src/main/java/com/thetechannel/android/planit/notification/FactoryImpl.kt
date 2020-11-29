package com.thetechannel.android.planit.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import com.thetechannel.android.planit.R
import com.thetechannel.android.planit.REMINDER_CHANNEL_ID
import com.thetechannel.android.planit.broadcast.AppBroadcastReceiver
import java.lang.Exception

const val REMINDER_NOTIFICATION_ID = 0

class AppNotificationFactory(
    override val context: Context
) : NotificationFactory {
    override fun create(name: String): Notification =
        when (name) {
            AppBroadcastReceiver.REMINDER_BROADCAST_EXTRA -> createReminderNotification()
            else -> throw Exception("Invalid notification request.")
        }

    private fun createReminderNotification(): Notification {
        return NotificationCompat.Builder(
            context,
            REMINDER_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.reminder_notication_title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}

class AppNotificationIdFactory(
    val name: String
) : NotificationIdFactory {
    override fun create(): Int =
        when(name) {
            AppBroadcastReceiver.REMINDER_BROADCAST_EXTRA -> REMINDER_NOTIFICATION_ID
            else -> throw Exception("invalid notification id")
        }
}