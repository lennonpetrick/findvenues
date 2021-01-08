package com.test.findvenues.repository

import com.test.findvenues.datasource.network.VenueNetworkDataSource
import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.mappers.VenueMapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class VenueRepository @Inject constructor(private val networkDataSource: VenueNetworkDataSource,
                                                   private val mapper: VenueMapper) {

    fun searchVenues(search: String, radius: Int, limit: Int): Single<List<Venue>> {
        return networkDataSource.searchVenues(search, radius, limit)
                .flattenAsObservable { it }
                .map(mapper::convert)
                .toList()
    }

    fun fetchVenue(venueId: String): Single<Venue> {
        return networkDataSource.fetchVenue(venueId)
                .map(mapper::convert)
    }

}