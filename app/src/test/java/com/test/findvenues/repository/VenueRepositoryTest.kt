package com.test.findvenues.repository

import androidx.room.rxjava3.EmptyResultSetException
import com.nhaarman.mockito_kotlin.whenever
import com.test.findvenues.datasource.local.VenueLocalDataSource
import com.test.findvenues.datasource.network.VenueNetworkDataSource
import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.entities.VenueEntity
import com.test.findvenues.repository.mappers.VenueMapper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueRepositoryTest {

    @Mock
    private lateinit var networkDataSource: VenueNetworkDataSource

    @Mock
    private lateinit var localDataSource: VenueLocalDataSource

    @Mock
    private lateinit var mapper: VenueMapper

    @Mock
    private lateinit var entity: VenueEntity

    @Mock
    private lateinit var venue: Venue

    @InjectMocks
    private lateinit var subject: VenueRepository

    @Test
    fun `When searching for venues for a specific place, then a list of venues are returned`() {
        val search = "whatever place"
        val radius = 1000
        val limit = 10
        val entities = listOf(entity)
        whenever(networkDataSource.searchVenues(search, radius, limit)).thenReturn(Single.just(entities))
        whenever(localDataSource.cacheSearch(search, entities)).thenReturn(Completable.complete())
        whenever(mapper.convert(entity)).thenReturn(venue)

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoErrors()
                .assertValue(listOf(venue))
    }

    @Test
    fun `When searching for venues fails, then try to get it from cache`() {
        val search = "whatever place"
        val radius = 1000
        val limit = 10
        whenever(networkDataSource.searchVenues(search, radius, limit)).thenReturn(Single.error(Exception()))
        whenever(localDataSource.getCachedSearch(search)).thenReturn(Single.just(listOf(entity)))
        whenever(mapper.convert(entity)).thenReturn(venue)

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoErrors()
                .assertValue(listOf(venue))
    }

    @Test
    fun `When searching for venues fails and it is also not cached, then an error is returned`() {
        val search = "whatever place"
        val radius = 1000
        val limit = 10
        val cacheException = Exception()
        whenever(networkDataSource.searchVenues(search, radius, limit)).thenReturn(Single.error(Exception()))
        whenever(localDataSource.getCachedSearch(search)).thenReturn(Single.error(cacheException))

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoValues()
                .assertError(cacheException)
    }

    @Test
    fun `When fetching a specific venue, then the venue is returned`() {
        val venueId = "venueId"
        whenever(networkDataSource.fetchVenue(venueId)).thenReturn(Single.just(entity))
        whenever(localDataSource.insertVenue(entity)).thenReturn(Completable.complete())
        whenever(mapper.convert(entity)).thenReturn(venue)

        subject.fetchVenue(venueId)
                .test()
                .assertNoErrors()
                .assertValue(venue)
    }

    @Test
    fun `When fetching a venue fails, then try to get it from database`() {
        val venueId = "venueId"
        whenever(networkDataSource.fetchVenue(venueId)).thenReturn(Single.error(Exception()))
        whenever(localDataSource.getVenue(venueId)).thenReturn(Single.just(entity))
        whenever(mapper.convert(entity)).thenReturn(venue)

        subject.fetchVenue(venueId)
                .test()
                .assertNoErrors()
                .assertValue(venue)
    }

    @Test
    fun `When fetching a venue fails and it is not in the database, then an error is returned`() {
        val venueId = "venueId"
        val exception = Exception()
        val dbException = EmptyResultSetException("message")
        whenever(networkDataSource.fetchVenue(venueId)).thenReturn(Single.error(exception))
        whenever(localDataSource.getVenue(venueId)).thenReturn(Single.error(dbException))

        subject.fetchVenue(venueId)
                .test()
                .assertNoValues()
                .assertError(dbException)
    }
}