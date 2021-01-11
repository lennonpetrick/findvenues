package com.test.findvenues

import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.entities.VenueEntity

internal object VenueFactory {

    fun createEntity(photosPerGroup: Int = 1, groupAmount: Int = 1): VenueEntity {
        val location = VenueEntity.Location("address", listOf("addresses"))
        val contact = VenueEntity.Contact("twitter", "instagram", "phone", "facebook")

        val items = (1..photosPerGroup).map { VenueEntity.Photos.Group.Item("prefix", "suffix") }
        val groups = (1..groupAmount).map { VenueEntity.Photos.Group(items) }
        val photos = VenueEntity.Photos(groups)

        return VenueEntity("id", "name", location, contact, "description", 10.0, photos)
    }

    fun createModel(addresses: List<String> = listOf("addresses")): Venue {
        val location = Venue.Location("address", addresses)
        val contact = Venue.Contact("twitter", "instagram", "phone", "facebook")
        val photos = mutableListOf("url")
        return Venue("id", "name", location, contact, "description", 10.0, photos)
    }

}