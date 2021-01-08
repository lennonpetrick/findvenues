package com.test.findvenues.repository.mappers

import com.test.findvenues.domain.models.Venue
import com.test.findvenues.repository.entities.VenueEntity
import javax.inject.Inject

internal class VenueMapper @Inject constructor() {

    fun convert(entity: VenueEntity): Venue {
        val location = entity.location.let { Venue.Location(it.address, it.formattedAddress) }
        val contact = entity.contact?.let { Venue.Contact(it.twitter, it.instagram, it.phone, it.facebook) }
        val photos = entity.photos?.let { mapPhotos(it) } ?: emptyList()
        return Venue(entity.id, entity.name, location, contact, entity.description, entity.rating, photos)
    }

    private fun mapPhotos(photos: VenueEntity.Photos): List<String> {
        return mutableListOf<String>().apply {
            photos.groups.forEach { group ->
                val items = group.items.map { "${it.prefix}original${it.suffix}" }
                addAll(items)
            }
        }
    }
}