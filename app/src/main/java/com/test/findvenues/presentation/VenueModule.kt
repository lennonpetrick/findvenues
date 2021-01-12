package com.test.findvenues.presentation

import android.content.Context
import android.content.SharedPreferences
import com.test.findvenues.datasource.local.VenueDao
import com.test.findvenues.datasource.local.database.AppDatabase
import com.test.findvenues.datasource.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit

@Module
internal class VenueModule(private val context: Context) {

    @Provides
    internal fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @Reusable
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Reusable
    fun providesVenueDao(database: AppDatabase): VenueDao {
        return database.venueDao()
    }

    @Provides
    @Reusable
    fun providesSharedPreferences(database: AppDatabase): SharedPreferences {
        return context.getSharedPreferences("find_venues", Context.MODE_PRIVATE)
    }
}