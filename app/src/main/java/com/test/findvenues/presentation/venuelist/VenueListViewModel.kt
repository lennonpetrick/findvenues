package com.test.findvenues.presentation.venuelist

import com.test.findvenues.presentation.BaseViewModel
import com.test.findvenues.repository.VenueRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject

internal class VenueListViewModel(private val repository: VenueRepository,
                                  private val mapper: VenueListViewMapper,
                                  private val radius: Int,
                                  private val limit: Int,
                                  workScheduler: Scheduler,
                                  uiScheduler: Scheduler,
                                  compositeDisposable: CompositeDisposable)
    : BaseViewModel(workScheduler, uiScheduler, compositeDisposable) {

    private val stateSubject = BehaviorSubject.create<ViewState>()
    internal val stateObservable: Observable<ViewState> = stateSubject

    fun searchVenues(search: String) {
        stateSubject.onNext(ViewState.Loading)
        repository.searchVenues(search, radius, limit)
                .flattenAsObservable { it }
                .map(mapper::convert)
                .toList()
                .applySchedulers()
                .subscribeBy(
                        onSuccess = { stateSubject.onNext(if (it.isEmpty()) ViewState.VenuesNotFound else ViewState.VenuesLoaded(it)) },
                        onError = { stateSubject.onNext(ViewState.Error(it.message)) }
                )
                .disposeInOnCleared()
    }

}

internal sealed class ViewState {
    internal object Loading : ViewState()
    internal class Error(val errorMessage: String?) : ViewState()
    internal class VenuesLoaded(val venues: List<VenueListView>) : ViewState()
    internal object VenuesNotFound: ViewState()
}