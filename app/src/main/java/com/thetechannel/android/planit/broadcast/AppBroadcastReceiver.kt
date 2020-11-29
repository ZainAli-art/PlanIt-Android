package com.thetechannel.android.planit.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.thetechannel.android.planit.notification.AppNotificationFactory
import com.thetechannel.android.planit.notification.AppNotificationIdFactory

private const val TAG = "AppBroadcastReceiver"

class AppBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "broadcast received")
        if (context == null || intent == null) return;

        val name = intent.getStringExtra(BROADCAST_NAME_KEY) ?: return
        val notification =
            AppNotificationFactory(context.applicationContext).create(name)
        val id = AppNotificationIdFactory(name).create()

        with(NotificationManagerCompat.from(context)) {
            notify(id, notification)
        }
    }

    companion object {
        const val BROADCAST_NAME_KEY = "BROADCAST_NAME_KEY"
        const val REMINDER_BROADCAST_EXTRA = "REMINDER_BROADCAST_EXTRA"
    }
}

