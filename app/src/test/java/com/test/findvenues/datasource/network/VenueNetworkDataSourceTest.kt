package com.test.findvenues.datasource.network

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.test.findvenues.repository.entities.VenueEntity
import com.test.findvenues.repository.entities.VenueListResponse
import com.test.findvenues.repository.entities.VenueResponse
import com.test.findvenues.repository.entities.Wrapper
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueNetworkDataSourceTest {

    companion object {
        private const val CLIENT_ID_KEY = "client_id"
        private const val CLIENT_ID = "client_id_value"

        private const val CLIENT_SECRET_KEY = "client_secret"
        private const val CLIENT_SECRET = "client_secret_value"

        private const val VERSION_KEY = "v"
        private const val VERSION = "version_value"
    }

    @Mock
    private lateinit var service: ApiService

    private lateinit var subject: VenueNetworkDataSource

    @BeforeEach
    fun setUp() {
        subject = VenueNetworkDataSource(CLIENT_ID, CLIENT_SECRET, VERSION, service)
    }

    @Test
    fun `When searching for venues for a specific place, then a list of venues are returned`() {
        val search = "whatever place"
        val radius = 1000
        val limit = 10
        val venueList = listOf(mock<VenueEntity>())

        val map = createMap().apply {
            put("near", search)
            put("radius", radius.toString())
            put("limit", limit.toString())
        }

        whenever(service.searchVenues(map)).thenReturn(Single.just(Wrapper(VenueListResponse(venueList))))

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoErrors()
                .assertValue(venueList)
    }

    @Test
    fun `When searching for venues fails, then an error is returned`() {
        val search = "whatever place"
        val radius = 1000
        val limit = 10
        val exception = Exception()

        val map = createMap().apply {
            put("near", search)
            put("radius", radius.toString())
            put("limit", limit.toString())
        }

        whenever(service.searchVenues(map)).thenReturn(Single.error(exception))

        subject.searchVenues(search, radius, limit)
                .test()
                .assertNoValues()
                .assertError(exception)
    }

    @Test
    fun `When fetching a specific venue, then the venue is returned`() {
        val venueId = "venueId"
        val venue = mock<VenueEntity>()
        whenever(service.fetchVenue(venueId, createMap())).thenReturn(Single.just(Wrapper(VenueResponse(venue))))

        subject.fetchVenue(venueId)
                .test()
                .assertNoErrors()
                .assertValue(venue)
    }

    @Test
    fun `When fetching a venue fails, then an error is returned`() {
        val venueId = "venueId"
        val exception = Exception()
        whenever(service.fetchVenue(venueId, createMap())).thenReturn(Single.error(exception))

        subject.fetchVenue(venueId)
                .test()
                .assertNoValues()
                .assertError(exception)
    }

    private fun createMap(): MutableMap<String, String> {
        return mutableMapOf(
                CLIENT_ID_KEY to CLIENT_ID,
                CLIENT_SECRET_KEY to CLIENT_SECRET,
                VERSION_KEY to VERSION)
    }
}