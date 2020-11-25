package com.thetechannel.android.planit

import android.app.Application
import com.thetechannel.android.planit.data.source.AppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    }
}