package com.test.findvenues.datasource.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.findvenues.repository.entities.VenueEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class VenueLocalDataSource @Inject constructor(private val venueDao: VenueDao,
                                                        private val sharedPreferences: SharedPreferences) {

    fun cacheSearch(search: String, entities: List<VenueEntity>): Completable {
        return Completable.fromCallable {
            sharedPreferences.edit().apply {
                putString(search.hashCode().toString(), Gson().toJson(entities))
                apply()
            }
        }
    }

    fun getCachedSearch(search: String): Single<List<VenueEntity>> {
        return Single.fromCallable {
            sharedPreferences.getString(search.hashCode().toString(), null)?.let {
                Gson().fromJson(it, object: TypeToken<List<VenueEntity>>() {}.type)
            } ?: emptyList()
        }
    }

    fun insertVenue(venue: VenueEntity): Completable {
        return venueDao.insertVenue(venue)
    }

    fun getVenue(venueId: String): Single<VenueEntity> {
        return venueDao.getVenue(venueId)
    }

}