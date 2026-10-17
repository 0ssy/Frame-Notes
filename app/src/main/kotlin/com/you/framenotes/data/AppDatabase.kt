package com.you.framenotes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PhotoEntity::class,
        PhotoNoteEntity::class,
        TagEntity::class,
        PhotoTagCrossRef::class,
        CollectionEntity::class,
        PhotoCollectionCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun noteDao(): NoteDao
}
