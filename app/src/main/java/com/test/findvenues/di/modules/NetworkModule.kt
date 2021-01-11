package com.test.findvenues.di.modules

import com.test.findvenues.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Named("clientId")
    @Provides
    fun providesClientId(): String {
        return BuildConfig.CLIENT_ID
    }

    @Named("clientSecret")
    @Provides
    fun providesClientSecret(): String {
        return BuildConfig.CLIENT_SECRET
    }

    @Named("version")
    @Provides
    fun providesVersion(): String {
        return "20210107"
    }

    @Provides
    @Reusable
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @Reusable
    fun providesRxJavaCallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @Provides
    @Reusable
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRetrofit(client: OkHttpClient,
                         rxAdapter: RxJava3CallAdapterFactory,
                         jsonConverter: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(rxAdapter)
                .addConverterFactory(jsonConverter)
                .client(client)
                .build()
    }

}