package com.test.findvenues.presentation.venuedetail

import com.test.findvenues.R
import com.test.findvenues.domain.models.Venue
import javax.inject.Inject

internal class VenueViewMapper @Inject constructor() {

    fun convert(venue: Venue): VenueView {

        val location = StringBuilder().apply {
            venue.location.formattedAddress.forEach { appendLine(it) }
        }.removeSuffix("\n").toString()

        val contacts = venue.contact?.let { contact ->
            mutableListOf<VenueView.DisplayText>().apply {
                contact.twitter?.let { add(VenueView.DisplayText(R.string.venue_detail_twitter, it)) }
                contact.instagram?.let { add(VenueView.DisplayText(R.string.venue_detail_instagram, it)) }
                contact.phone?.let { add(VenueView.DisplayText(R.string.venue_detail_phone, it)) }
                contact.facebook?.let { add(VenueView.DisplayText(R.string.venue_detail_facebook, it)) }
            }
        } ?: emptyList()

        return VenueView(venue.id, venue.name, location, contacts, venue.description, venue.rating.toString(), venue.photos)
    }
}