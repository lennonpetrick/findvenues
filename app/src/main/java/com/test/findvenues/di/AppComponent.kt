package com.test.findvenues.di

import com.test.findvenues.di.modules.NetworkModule
import com.test.findvenues.di.modules.SchedulerModule
import com.test.findvenues.di.qualifiers.IOScheduler
import com.test.findvenues.di.qualifiers.UIScheduler
import dagger.Component
import io.reactivex.rxjava3.core.Scheduler
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, SchedulerModule::class])
internal interface AppComponent {

    @IOScheduler
    fun ioScheduler(): Scheduler

    @UIScheduler
    fun uiScheduler(): Scheduler

    @Named("clientId")
    fun clientId(): String

    @Named("clientSecret")
    fun clientSecret(): String

    @Named("version")
    fun version(): String

    fun retrofit(): Retrofit

}