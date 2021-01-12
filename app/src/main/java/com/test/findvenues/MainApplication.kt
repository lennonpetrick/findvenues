package com.test.findvenues

import android.app.Application
import com.test.findvenues.di.Components

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Components.init(this)
    }

}