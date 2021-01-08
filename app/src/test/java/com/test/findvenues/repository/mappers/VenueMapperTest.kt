package com.test.findvenues.repository.mappers

import com.test.findvenues.VenueFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueMapperTest {

    private val subject = VenueMapper()

    @Test
    fun `When converting an entity to a model, then it converts properly`() {
        val entity = VenueFactory.createEntity()

        val model = subject.convert(entity)

        assertAll("Venue fields", {
            assertEquals(entity.id, model.id)
            assertEquals(entity.name, model.name)
            assertEquals(entity.description, model.description)
            assertEquals(entity.rating, model.rating)

            val locationEntity = entity.location
            with(model.location) {
                assertEquals(locationEntity.address, address)
                assertEquals(locationEntity.formattedAddress, formattedAddress)
            }

            val contactEntity = entity.contact!!
            with(model.contact!!) {
                assertEquals(contactEntity.twitter, twitter)
                assertEquals(contactEntity.instagram, instagram)
                assertEquals(contactEntity.phone, phone)
                assertEquals(contactEntity.facebook, facebook)
            }

            val expectedUrl = entity.photos!!.groups[0].items[0].let { "${it.prefix}original${it.suffix}" }
            assertEquals(expectedUrl, model.photos[0])
        })
    }

    @ParameterizedTest
    @ValueSource(ints = [2, 3, 5])
    fun `When there are more photos per group, then it returns all into a single list`(amount: Int) {
        val entity = VenueFactory.createEntity(amount, amount)

        val model = subject.convert(entity)

        assertEquals(amount * amount, model.photos.size)
    }
}