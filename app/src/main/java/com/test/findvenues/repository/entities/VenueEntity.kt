package com.test.findvenues.repository.entities

import com.google.gson.annotations.SerializedName

internal class Wrapper<T>(val response: T)
internal class VenueListResponse(val venues: List<VenueEntity>)
internal class VenueResponse(val venue: VenueEntity)

internal data class VenueEntity(
        val id: String,
        val name: String,
        val location: Location,
        val contact: Contact?,
        val description: String?,
        val rating: Double?,
        val photos: Photos?) {

    class Contact(
            val twitter: String?,
            val instagram: String?,

            @SerializedName("formattedPhone")
            val phone: String?,

            @SerializedName("facebookName")
            val facebook: String?)

    class Location(val address: String?, val formattedAddress: List<String>)

    class Photos(val groups: List<Group>) {
        class Group(val items: List<Item>) {
            class Item(val prefix: String, val suffix: String)
        }
    }
}