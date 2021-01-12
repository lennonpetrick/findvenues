package com.test.findvenues.datasource.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.test.findvenues.VenueFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VenueLocalDataSourceTest {

    @Mock
    private lateinit var venueDao: VenueDao

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @InjectMocks
    private lateinit var subject: VenueLocalDataSource

    @Test
    fun `When caching search result, then venues are cached with the hash of the search`() {
        val editor = mock<SharedPreferences.Editor>()
        whenever(sharedPreferences.edit()).thenReturn(editor)

        val search = "whatever search"
        val entities = listOf(VenueFactory.createEntity())
        subject.cacheSearch(search, entities)
                .test()
                .assertNoErrors()
                .assertComplete()

        verify(editor).putString(search.hashCode().toString(), Gson().toJson(entities))
    }

    @Test
    fun `When getting the cached search, then a list of venues are returned`() {
        val search = "whatever search"
        val entities = listOf(VenueFactory.createEntity())
        val json = Gson().toJson(entities)
        whenever(sharedPreferences.getString(search.hashCode().toString(), null)).thenReturn(json)

        subject.getCachedSearch(search)
                .test()
                .assertNoErrors()
                .assertValue { it.first().id == entities.first().id }
    }

    @Test
    fun `When insert a venue, then it is inserted in the database`() {
        val entity = VenueFactory.createEntity()
        whenever(venueDao.insertVenue(entity)).thenReturn(Completable.complete())

        subject.insertVenue(entity)
                .test()
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun `When getting the venue from database, then the venue is returned`() {
        val venueId = "venue id"
        val entity = VenueFactory.createEntity()
        whenever(venueDao.getVenue(venueId)).thenReturn(Single.just(entity))

        subject.getVenue(venueId)
                .test()
                .assertNoErrors()
                .assertValue(entity)
    }
}