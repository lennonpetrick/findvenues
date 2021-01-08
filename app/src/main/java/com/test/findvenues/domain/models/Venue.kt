package com.test.findvenues.domain.models

internal class Venue(
        val id: String,
        val name: String,
        val location: Location,
        val contact: Contact?,
        val description: String?,
        val rating: Double?,
        val photos: List<String>) {

    class Location(val address: String?, val formattedAddress: List<String>)

    class Contact(
            val twitter: String?,
            val instagram: String?,
            val phone: String?,
            val facebook: String?)
}