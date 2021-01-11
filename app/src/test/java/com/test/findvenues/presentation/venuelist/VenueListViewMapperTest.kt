package com.test.findvenues.presentation.venuelist

import com.test.findvenues.VenueFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueListViewMapperTest {

    private val subject = VenueListViewMapper()

    @Test
    fun `When converting the model to the view , then it converts properly`() {
        val model = VenueFactory.createModel()

        val view = subject.convert(model)

        assertEquals(model.id, view.id)
        assertEquals(model.name, view.name)
    }
}