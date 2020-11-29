package com.thetechannel.android.planit

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.thetechannel.android.planit.data.source.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val REMINDER_CHANNEL_ID = "com.thetechannel.android.planit.newtask.REMINDER_CHANNEL_ID"

class MyApplication : Application() {
    val repository : AppRepository
        get() = ServiceLocator.provideRepository(this)

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        scope.launch {
            repository.refreshCategories()
            repository.refreshTaskMethods()
            repository.refreshTasks()
        }
        createReminderNotificationChannel()
    }

    private fun createReminderNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;

        val name = getString(R.string.reminder_channel_name)
        val descriptionText = getString(R.string.reminder_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(REMINDER_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}