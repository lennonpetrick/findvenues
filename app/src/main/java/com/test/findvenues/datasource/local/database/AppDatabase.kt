package com.test.findvenues.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.findvenues.datasource.local.VenueDao
import com.test.findvenues.repository.entities.VenueEntity

@Database(entities = [VenueEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}