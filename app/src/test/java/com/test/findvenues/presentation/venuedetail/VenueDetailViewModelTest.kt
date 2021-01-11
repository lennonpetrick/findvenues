package com.test.findvenues.presentation.venuedetail

import com.nhaarman.mockito_kotlin.mock
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
internal class VenueDetailViewModelTest {

    companion object {
        private const val VENUE_ID = "venue_id"
    }

    @Mock
    private lateinit var repository: VenueRepository

    @Mock
    private lateinit var mapper: VenueViewMapper

    @Mock
    private lateinit var venue: Venue

    @Mock
    private lateinit var venueView: VenueView

    private val compositeDisposable = CompositeDisposable()
    private val scheduler = Schedulers.trampoline()

    private lateinit var subject: VenueDetailViewModel

    @BeforeEach
    fun setUp() {
        subject = VenueDetailViewModel(VENUE_ID, repository, mapper, scheduler, scheduler, compositeDisposable)
    }

    @Test
    fun `When on create is called, then the venue is fetched`() {
        whenever(repository.fetchVenue(VENUE_ID)).thenReturn(Single.just(venue))
        whenever(mapper.convert(venue)).thenReturn(venueView)

        val stateObservable = subject.stateObservable.test()
        subject.onCreate(mock())

        stateObservable.assertNoErrors()
                .assertLastValue { state -> state is ViewState.Loaded && state.venue == venueView }
    }

    @Test
    fun `When fetching the venue, then a loading state is emitted`() {
        whenever(repository.fetchVenue(VENUE_ID)).thenReturn(Single.just(venue))
        whenever(mapper.convert(venue)).thenReturn(venueView)

        val stateObservable = subject.stateObservable.test()
        subject.onCreate(mock())

        stateObservable.assertNoErrors()
                .assertValueAt(0) { state -> state is ViewState.Loading }
    }

    @Test
    fun `When fetching the venue fails, then an error state is emitted`() {
        val errorMessage = "errorMessage"
        whenever(repository.fetchVenue(VENUE_ID)).thenReturn(Single.error(Throwable(errorMessage)))

        val stateObservable = subject.stateObservable.test()
        subject.onCreate(mock())

        stateObservable.assertNoErrors()
                .assertLastValue { state -> state is ViewState.Error && state.errorMessage == errorMessage }
    }
}