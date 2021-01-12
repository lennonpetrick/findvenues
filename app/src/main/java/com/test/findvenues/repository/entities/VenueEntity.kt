package com.test.findvenues.repository.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.test.findvenues.datasource.local.database.ListGroupTypeConverter
import com.test.findvenues.datasource.local.database.ListStringTypeConverter

internal class Wrapper<T>(val response: T)
internal class VenueListResponse(val venues: List<VenueEntity>)
internal class VenueResponse(val venue: VenueEntity)

@Entity
internal data class VenueEntity(
        @PrimaryKey val id: String,
        val name: String,
        @Embedded val location: Location,
        @Embedded val contact: Contact?,
        val description: String?,
        val rating: Double?,
        @Embedded val photos: Photos?) {

    class Contact(
            val twitter: String?,
            val instagram: String?,

            @SerializedName("formattedPhone")
            val phone: String?,

            @SerializedName("facebookName")
            val facebook: String?)

    @TypeConverters(ListStringTypeConverter::class)
    class Location(val address: String?, val formattedAddress: List<String>)

    @TypeConverters(ListGroupTypeConverter::class)
    class Photos(val groups: List<Group>) {
        class Group(val items: List<Item>) {
            class Item(val prefix: String, val suffix: String)
        }
    }
}