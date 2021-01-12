package com.test.findvenues.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.findvenues.repository.entities.VenueEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
internal interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenue(venue: VenueEntity): Completable

    @Query("SELECT * FROM VenueEntity WHERE id=:venueId")
    fun getVenue(venueId: String): Single<VenueEntity>

}