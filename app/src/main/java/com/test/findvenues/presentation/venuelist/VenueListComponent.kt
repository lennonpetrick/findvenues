package com.test.findvenues.presentation.venuelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.findvenues.datasource.network.ApiService
import com.test.findvenues.di.AppComponent
import com.test.findvenues.di.FeatureScope
import com.test.findvenues.di.qualifiers.IOScheduler
import com.test.findvenues.di.qualifiers.UIScheduler
import com.test.findvenues.repository.VenueRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

@Module
internal class VenueListModule {

    @Named("radius")
    @Provides
    fun providesRadius(): Int {
        return 1000
    }

    @Named("limit")
    @Provides
    fun providesLimit(): Int {
        return 10
    }

    @Provides
    internal fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @Reusable
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [VenueListModule::class])
internal interface VenueListComponent {
    fun venueListViewModelFactory(): VenueListViewModelFactory
}

internal class VenueListViewModelFactory @Inject constructor(private val repository: VenueRepository,
                                                             private val mapper: VenueListViewMapper,
                                                             @Named("radius") private val radius: Int,
                                                             @Named("limit") private val limit: Int,
                                                             @IOScheduler private val workScheduler: Scheduler,
                                                             @UIScheduler private val uiScheduler: Scheduler,
                                                             private val compositeDisposable: CompositeDisposable)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VenueListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VenueListViewModel(repository, mapper, radius, limit, workScheduler,
                    uiScheduler, compositeDisposable) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}