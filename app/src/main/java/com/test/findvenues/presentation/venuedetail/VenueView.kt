package com.test.findvenues.presentation.venuedetail

import androidx.annotation.StringRes

internal class VenueView(
        val id: String,
        val name: String,
        val location: String?,
        val contacts: List<DisplayText>,
        val description: String?,
        val rating: String?,
        val photos: List<String>) {

    class DisplayText(@StringRes val stringRes: Int, val value: String)
}