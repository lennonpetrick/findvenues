package com.test.findvenues.di

import com.test.findvenues.di.modules.NetworkModule
import com.test.findvenues.di.modules.SchedulerModule

internal object Components {

    private lateinit var appComponent: AppComponent

    fun init() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule())
                .schedulerModule(SchedulerModule())
                .build()
    }

    fun appComponent() = appComponent

}