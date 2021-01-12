package com.test.findvenues.presentation.venuedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.findvenues.di.AppComponent
import com.test.findvenues.di.FeatureScope
import com.test.findvenues.di.qualifiers.IOScheduler
import com.test.findvenues.di.qualifiers.UIScheduler
import com.test.findvenues.presentation.VenueModule
import com.test.findvenues.repository.VenueRepository
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

@Module
internal class VenueDetailModule(private val venueId: String) {

    @Named("venueId")
    @Provides
    fun providesVenueId(): String {
        return venueId
    }

}

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [VenueDetailModule::class, VenueModule::class])
internal interface VenueDetailComponent {
    fun venueDetailViewModelFactory(): VenueDetailViewModelFactory
}

internal class VenueDetailViewModelFactory @Inject constructor(@Named("venueId") private val venueId: String,
                                                               private val repository: VenueRepository,
                                                               private val mapper: VenueViewMapper,
                                                               @IOScheduler private val workScheduler: Scheduler,
                                                               @UIScheduler private val uiScheduler: Scheduler,
                                                               private val compositeDisposable: CompositeDisposable)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VenueDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VenueDetailViewModel(venueId, repository, mapper, workScheduler, uiScheduler, compositeDisposable) as T
        }
        throw IllegalStateException("Unknown ViewModel class")
    }
}