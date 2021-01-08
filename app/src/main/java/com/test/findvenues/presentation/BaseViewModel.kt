package com.test.findvenues.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

internal open class BaseViewModel(private val workScheduler: Scheduler,
                                  private val uiScheduler: Scheduler,
                                  private val compositeDisposable: CompositeDisposable) : ViewModel() {

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected fun Disposable.disposeInOnCleared(): Disposable {
        return this.also { compositeDisposable.add(it) }
    }

    protected fun <T> Single<T>.applySchedulers(): Single<T> {
        return this.subscribeOn(workScheduler)
                .observeOn(uiScheduler)
    }
}