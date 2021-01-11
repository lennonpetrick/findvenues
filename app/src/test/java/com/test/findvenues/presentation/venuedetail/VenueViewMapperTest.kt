package com.test.findvenues.presentation.venuedetail

import com.test.findvenues.R
import com.test.findvenues.VenueFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueViewMapperTest {

    private val subject = VenueViewMapper()

    @Test
    fun `When converting the model to the view , then it converts properly`() {
        val address1 = "address1"
        val address2 = "address2"
        val model = VenueFactory.createModel(listOf(address1, address2))

        val view = subject.convert(model)

        assertAll("Venue view fields", {
            assertEquals(model.id, view.id)
            assertEquals(model.name, view.name)
            assertEquals(model.description, view.description)
            assertEquals(model.rating.toString(), view.rating)
            assertEquals(model.photos, view.photos)
            assertEquals(address1.plus("\n").plus(address2), view.location)

            with(model.contact!!) {
                assertEquals(R.string.venue_detail_twitter, view.contacts[0].stringRes)
                assertEquals(twitter, view.contacts[0].value)

                assertEquals(R.string.venue_detail_instagram, view.contacts[1].stringRes)
                assertEquals(instagram, view.contacts[1].value)

                assertEquals(R.string.venue_detail_phone, view.contacts[2].stringRes)
                assertEquals(phone, view.contacts[2].value)

                assertEquals(R.string.venue_detail_facebook, view.contacts[3].stringRes)
                assertEquals(facebook, view.contacts[3].value)
            }
        })
    }

}