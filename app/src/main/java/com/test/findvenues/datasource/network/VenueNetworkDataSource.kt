package com.test.findvenues.datasource.network

import com.test.findvenues.repository.entities.VenueEntity
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

internal class VenueNetworkDataSource @Inject constructor(@Named("clientId") private val clientId: String,
                                                          @Named("clientSecret") private val clientSecret: String,
                                                          @Named("version") private val version: String,
                                                          private val service: ApiService) {

    fun searchVenues(search: String, radius: Int, limit: Int): Single<List<VenueEntity>> {
        val queryMap = createQueryMap().apply {
            put("near", search)
            put("radius", radius.toString())
            put("limit", limit.toString())
        }

        return service.searchVenues(queryMap)
                .map { it.response.venues }
    }

    fun fetchVenue(venueId: String): Single<VenueEntity> {
        return service.fetchVenue(venueId, createQueryMap())
                .map { it.response.venue }
    }

    private fun createQueryMap(): MutableMap<String, String> {
        return mutableMapOf(
                "client_id" to clientId,
                "client_secret" to clientSecret,
                "v" to version)
    }
}