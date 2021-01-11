package com.test.findvenues.presentation.venuelist

import com.test.findvenues.domain.models.Venue
import javax.inject.Inject

internal data class VenueListView(
        val id: String,
        val name: String)

internal class VenueListViewMapper @Inject constructor() {
    fun convert(venue: Venue): VenueListView {
        return VenueListView(venue.id, venue.name)
    }
}