package com.test.findvenues.di.modules

import android.content.Context
import androidx.room.Room
import com.test.findvenues.datasource.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DatabaseModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    fun providesAppDatabase(): AppDatabase {
        return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "findvenues.db")
                .build()
    }
}