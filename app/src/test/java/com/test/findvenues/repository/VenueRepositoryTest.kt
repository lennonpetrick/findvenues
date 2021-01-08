package com.test.findvenues.repository

import com.nhaarman.mockito_kotlin.whenever
import com.test.findvenues.datasource.network.VenueNetworkDataSource
import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.entities.VenueEntity
import com.test.findvenues.repository.mappers.VenueMapper
import io.reactivex.Single
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
        whenever(networkDataSource.searchVenues(search, radius, limit)).thenReturn(Single.just(listOf(entity)))
        whenever(mapper.convert(entity)).thenReturn(venue)

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoErrors()
                .assertValue(listOf(venue))
    }

    @Test
    fun `When searching for venues fails, then an error is returned`() {
        val search = "whatever place"
        val radius = 1000
        val limit = 10
        val exception = Exception()
        whenever(networkDataSource.searchVenues(search, radius, limit)).thenReturn(Single.error(exception))

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoValues()
                .assertError(exception)
    }

    @Test
    fun `When fetching a specific venue, then the venue is returned`() {
        val venueId = "venueId"
        whenever(networkDataSource.fetchVenue(venueId)).thenReturn(Single.just(entity))
        whenever(mapper.convert(entity)).thenReturn(venue)

        subject.fetchVenue(venueId)
                .test()
                .assertNoErrors()
                .assertValue(venue)
    }

    @Test
    fun `When fetching a venue fails, then an error is returned`() {
        val venueId = "venueId"
        val exception = Exception()
        whenever(networkDataSource.fetchVenue(venueId)).thenReturn(Single.error(exception))

        subject.fetchVenue(venueId)
                .test()
                .assertNoValues()
                .assertError(exception)
    }
}