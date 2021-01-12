package com.test.findvenues.datasource.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.test.findvenues.repository.entities.VenueEntity

internal class ListStringTypeConverter {
    @TypeConverter
    fun fromString(json: String): List<String> = Gson().fromJson(json, object: TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun toString(list: List<String>): String = Gson().toJson(list)
}

internal class ListGroupTypeConverter {
    @TypeConverter
    fun fromString(json: String): List<VenueEntity.Photos.Group> =
            Gson().fromJson(json, object: TypeToken<List<VenueEntity.Photos.Group>>() {}.type)

    @TypeConverter
    fun toString(list: List<VenueEntity.Photos.Group>): String = Gson().toJson(list)
}