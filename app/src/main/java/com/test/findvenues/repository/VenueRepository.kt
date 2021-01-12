package com.test.findvenues.repository

import com.test.findvenues.datasource.local.VenueLocalDataSource
import com.test.findvenues.datasource.network.VenueNetworkDataSource
import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.entities.VenueEntity
import com.test.findvenues.repository.mappers.VenueMapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class VenueRepository @Inject constructor(private val networkDataSource: VenueNetworkDataSource,
                                                   private val localDataSource: VenueLocalDataSource,
                                                   private val mapper: VenueMapper) {

    fun searchVenues(search: String, radius: Int, limit: Int): Single<List<Venue>> {
        return searchOnNetwork(search, radius, limit)
                .onErrorResumeNext { localDataSource.getCachedSearch(search) }
                .flattenAsObservable { it }
                .map(mapper::convert)
                .toList()
    }

    private fun searchOnNetwork(search: String, radius: Int, limit: Int): Single<List<VenueEntity>> {
        return networkDataSource.searchVenues(search, radius, limit)
                .flatMap { localDataSource.cacheSearch(search, it).toSingleDefault(it) }
    }

    fun fetchVenue(venueId: String): Single<Venue> {
        return fetchVenueOnNetwork(venueId)
                .onErrorResumeNext { localDataSource.getVenue(venueId) }
                .map(mapper::convert)
    }

    private fun fetchVenueOnNetwork(venueId: String): Single<VenueEntity> {
        return networkDataSource.fetchVenue(venueId)
                .flatMap { localDataSource.insertVenue(it).toSingleDefault(it) }
    }

}