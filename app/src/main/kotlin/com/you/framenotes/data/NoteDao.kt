package com.you.framenotes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class NoteDao {
    @Query("SELECT * FROM photo_notes WHERE photoId = :photoId")
    abstract fun note(photoId: Long): Flow<PhotoNoteEntity?>

    @Upsert abstract suspend fun saveNote(note: PhotoNoteEntity)

    @Query("""SELECT t.* FROM tags t JOIN photo_tags pt ON pt.tagId = t.id
              WHERE pt.photoId = :photoId ORDER BY t.name""")
    abstract fun tagsFor(photoId: Long): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags ORDER BY name")
    abstract fun allTags(): Flow<List<TagEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) abstract suspend fun insertTag(t: TagEntity): Long
    @Query("SELECT id FROM tags WHERE name = :name") abstract suspend fun tagId(name: String): Long?
    @Insert(onConflict = OnConflictStrategy.IGNORE) abstract suspend fun link(ref: PhotoTagCrossRef)
    @Query("DELETE FROM photo_tags WHERE photoId = :photoId AND tagId = :tagId")
    abstract suspend fun unlink(photoId: Long, tagId: Long)

    @Transaction
    open suspend fun addTag(photoId: Long, rawName: String) {
        val name = rawName.trim().lowercase()
        if (name.isEmpty()) return
        val id = tagId(name) ?: insertTag(TagEntity(name = name))
        link(PhotoTagCrossRef(photoId, id))
    }
}
