package com.you.framenotes.data

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun db(@ApplicationContext c: Context) =
        Room.databaseBuilder(c, AppDatabase::class.java, "framenotes.db")
            .build()

    @Provides fun photoDao(db: AppDatabase) = db.photoDao()
    @Provides fun noteDao(db: AppDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun workManager(@ApplicationContext c: Context): WorkManager = WorkManager.getInstance(c)
}

@Singleton
class FolderPrefs @Inject constructor(@ApplicationContext c: Context) {
    private val sp = c.getSharedPreferences("folder", Context.MODE_PRIVATE)
    fun treeUri(): Uri? = sp.getString("tree", null)?.let(Uri::parse)
    fun save(uri: Uri) = sp.edit().putString("tree", uri.toString()).apply()
}
