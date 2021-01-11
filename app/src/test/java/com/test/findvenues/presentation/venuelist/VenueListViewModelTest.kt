package com.test.findvenues.presentation.venuelist

import com.nhaarman.mockito_kotlin.whenever
import com.test.findvenues.assertLastValue
import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.VenueRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueListViewModelTest {

    companion object {
        private const val RADIUS = 1000
        private const val LIMIT = 10
    }

    @Mock
    private lateinit var repository: VenueRepository

    @Mock
    private lateinit var mapper: VenueListViewMapper

    @Mock
    private lateinit var venue: Venue

    @Mock
    private lateinit var venueListView: VenueListView

    private val compositeDisposable = CompositeDisposable()
    private val scheduler = Schedulers.trampoline()

    private lateinit var subject: VenueListViewModel

    @BeforeEach
    fun setUp() {
        subject = VenueListViewModel(repository, mapper, RADIUS, LIMIT, scheduler, scheduler, compositeDisposable)
    }

    @Test
    fun `When searching for venues, then a loading state is emitted`() {
        val search = "whatever place"
        whenever(repository.searchVenues(search, RADIUS, LIMIT)).thenReturn(Single.just(listOf(venue)))
        whenever(mapper.convert(venue)).thenReturn(venueListView)

        val stateObservable = subject.stateObservable.test()
        subject.searchVenues(search)

        stateObservable.assertNoErrors()
                .assertValueAt(0) { state -> state is ViewState.Loading }
    }

    @Test
    fun `When the searching succeeds, then a loaded state is emitted with a list of venues`() {
        val search = "whatever place"
        whenever(repository.searchVenues(search, RADIUS, LIMIT)).thenReturn(Single.just(listOf(venue)))
        whenever(mapper.convert(venue)).thenReturn(venueListView)

        val stateObservable = subject.stateObservable.test()
        subject.searchVenues(search)

        stateObservable.assertNoErrors()
                .assertLastValue { state -> state is ViewState.Loaded && state.venues == listOf(venueListView) }
    }

    @Test
    fun `When the searching fails, then an error state is emitted`() {
        val search = "whatever place"
        val errorMessage = "errorMessage"
        whenever(repository.searchVenues(search, RADIUS, LIMIT)).thenReturn(Single.error(Throwable(errorMessage)))

        val stateObservable = subject.stateObservable.test()
        subject.searchVenues(search)

        stateObservable.assertNoErrors()
                .assertLastValue { state -> state is ViewState.Error && state.errorMessage == errorMessage }
    }

}