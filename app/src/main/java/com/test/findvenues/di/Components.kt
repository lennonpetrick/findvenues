package com.test.findvenues.di

import android.content.Context
import com.test.findvenues.di.modules.DatabaseModule
import com.test.findvenues.di.modules.NetworkModule
import com.test.findvenues.di.modules.SchedulerModule

internal object Components {

    private lateinit var appComponent: AppComponent

    fun init(applicationContext: Context) {
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule())
                .schedulerModule(SchedulerModule())
                .databaseModule(DatabaseModule(applicationContext))
                .build()
    }

    fun appComponent() = appComponent

}