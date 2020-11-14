package com.thetechannel.android.planit

import android.app.Application
import com.thetechannel.android.planit.data.source.AppRepository

class MyApplication : Application() {
    val repository : AppRepository
        get() = ServiceLocator.provideRepository(this)
}