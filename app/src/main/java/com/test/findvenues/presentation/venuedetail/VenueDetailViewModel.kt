package com.test.findvenues.presentation.venuedetail

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.test.findvenues.presentation.BaseViewModel
import com.test.findvenues.repository.VenueRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject

internal class VenueDetailViewModel(private val venueId: String,
                                    private val repository: VenueRepository,
                                    private val mapper: VenueViewMapper,
                                    workScheduler: Scheduler,
                                    uiScheduler: Scheduler,
                                    compositeDisposable: CompositeDisposable)
    : BaseViewModel(workScheduler, uiScheduler, compositeDisposable), DefaultLifecycleObserver {

    private val stateSubject = BehaviorSubject.create<ViewState>()
    internal val stateObservable: Observable<ViewState> = stateSubject

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchVenue()
    }

    private fun fetchVenue() {
        stateSubject.onNext(ViewState.Loading)
        repository.fetchVenue(venueId)
                .map(mapper::convert)
                .applySchedulers()
                .subscribeBy(
                        onSuccess = { stateSubject.onNext(ViewState.Loaded(it)) },
                        onError = { stateSubject.onNext(ViewState.Error(it.message)) }
                )
                .disposeInOnCleared()
    }
}

internal sealed class ViewState {
    internal object Loading : ViewState()
    internal class Error(val errorMessage: String?) : ViewState()
    internal class Loaded(val venue: VenueView) : ViewState()
}