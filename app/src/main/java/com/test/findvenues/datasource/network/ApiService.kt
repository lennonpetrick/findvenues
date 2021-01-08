package com.test.findvenues.datasource.network

import com.test.findvenues.repository.entities.VenueListResponse
import com.test.findvenues.repository.entities.VenueResponse
import com.test.findvenues.repository.entities.Wrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface ApiService {

    @GET("venues/search")
    fun searchVenues(@QueryMap queries: Map<String, String>): Single<Wrapper<VenueListResponse>>

    @GET("venues/{venueId}")
    fun fetchVenue(@Path("venueId") venueId: String,
                   @QueryMap queries: Map<String, String>): Single<Wrapper<VenueResponse>>
}