// PhotoDaoTest.kt (instrumented, in-memory DB)
package com.you.framenotes.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDaoTest {
    private lateinit var db: AppDatabase
    @Before fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
    @After fun tearDown() = db.close()

    private fun photo(uri: String, cam: String, focal35: Int) = PhotoEntity(
        uri = uri, displayName = uri, lastModified = 1, sizeBytes = 1, camera = cam, lens = null,
        focalLength = null, focalLength35 = focal35, aperture = null, shutterSeconds = null,
        iso = 100, takenAt = 0, width = null, height = null, latitude = null, longitude = null
    )

    @Test fun focalBuckets_groupCorrectly() = runBlocking {
        db.photoDao().upsert(listOf(photo("a", "X", 28), photo("b", "X", 30), photo("c", "Y", 50)))
        val rows = db.photoDao().focalBuckets().first()
        val bucket1 = rows.firstOrNull { it.bucket == 1 }
        val bucket3 = rows.firstOrNull { it.bucket == 3 }
        assertEquals(2, bucket1?.count)
        assertEquals(1, bucket3?.count)
    }

    @Test fun upsert_keepsNotes_whenPhotoUpdated() = runBlocking {
        db.photoDao().upsert(listOf(photo("a", "X", 35)))
        val id = db.photoDao().snapshot().single().id
        db.noteDao().saveNote(PhotoNoteEntity(id, "keep me", 0))
        db.photoDao().upsert(listOf(photo("a", "X", 35).copy(id = id, lastModified = 2)))
        assertEquals("keep me", db.noteDao().note(id).first()?.text)
    }
}
