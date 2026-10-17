package com.you.framenotes.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

data class PhotoStamp(val id: Long, val uri: String, val lastModified: Long, val missing: Boolean)
data class Bucket(val bucket: Int, val count: Int)

@Dao
interface PhotoDao {
    @Query("SELECT id, uri, lastModified, missing FROM photos")
    suspend fun snapshot(): List<PhotoStamp>

    @Upsert suspend fun upsert(photos: List<PhotoEntity>)

    @Query("UPDATE photos SET missing = 1 WHERE id IN (:ids)")
    suspend fun markMissing(ids: List<Long>)

    @Query("SELECT * FROM photos WHERE id = :id")
    fun photo(id: Long): Flow<PhotoEntity?>

    @Query("SELECT * FROM photos WHERE missing = 0 ORDER BY takenAt")
    suspend fun allForExport(): List<PhotoEntity>

    @Query("SELECT DISTINCT camera FROM photos WHERE camera IS NOT NULL AND missing = 0 ORDER BY camera")
    fun cameras(): Flow<List<String>>

    @Query("""
        SELECT * FROM photos
        WHERE missing = 0
          AND (:camera IS NULL OR camera = :camera)
          AND (:minFocal IS NULL OR focalLength35 >= :minFocal)
          AND (:maxFocal IS NULL OR focalLength35 <= :maxFocal)
          AND (:tagId IS NULL OR id IN (SELECT photoId FROM photo_tags WHERE tagId = :tagId))
          AND (:query = '' OR displayName LIKE '%' || :query || '%'
               OR camera LIKE '%' || :query || '%'
               OR id IN (SELECT photoId FROM photo_notes WHERE text LIKE '%' || :query || '%')
               OR id IN (SELECT pt.photoId FROM photo_tags pt JOIN tags t ON t.id = pt.tagId
                         WHERE t.name LIKE '%' || :query || '%'))
        ORDER BY
          CASE WHEN :sort = 0 THEN takenAt END DESC,
          CASE WHEN :sort = 1 THEN takenAt END ASC,
          CASE WHEN :sort = 2 THEN focalLength35 END ASC,
          CASE WHEN :sort = 3 THEN iso END DESC,
          id DESC
    """)
    fun paged(
        camera: String?, minFocal: Int?, maxFocal: Int?,
        tagId: Long?, query: String, sort: Int
    ): PagingSource<Int, PhotoEntity>

    @Query("SELECT COUNT(*) FROM photos WHERE missing = 0") fun total(): Flow<Int>

    @Query("SELECT COUNT(DISTINCT camera) FROM photos WHERE missing = 0 AND camera IS NOT NULL")
    fun cameraCount(): Flow<Int>

    @Query("""SELECT focalLength35 FROM photos
              WHERE missing = 0 AND focalLength35 IS NOT NULL
              GROUP BY focalLength35 ORDER BY COUNT(*) DESC LIMIT 1""")
    fun topFocal(): Flow<Int?>

    @Query("""
        SELECT CASE WHEN focalLength35 < 24 THEN 0 WHEN focalLength35 < 35 THEN 1
                    WHEN focalLength35 < 50 THEN 2 WHEN focalLength35 < 85 THEN 3
                    WHEN focalLength35 < 135 THEN 4 ELSE 5 END AS bucket,
               COUNT(*) AS count
        FROM photos WHERE missing = 0 AND focalLength35 IS NOT NULL
        GROUP BY bucket ORDER BY bucket""")
    fun focalBuckets(): Flow<List<Bucket>>

    @Query("""
        SELECT CASE WHEN iso <= 100 THEN 0 WHEN iso <= 200 THEN 1 WHEN iso <= 400 THEN 2
                    WHEN iso <= 800 THEN 3 WHEN iso <= 1600 THEN 4 WHEN iso <= 3200 THEN 5
                    ELSE 6 END AS bucket,
               COUNT(*) AS count
        FROM photos WHERE missing = 0 AND iso IS NOT NULL
        GROUP BY bucket ORDER BY bucket""")
    fun isoBuckets(): Flow<List<Bucket>>

    @Query("""
        SELECT CAST(strftime('%H', takenAt / 1000, 'unixepoch') AS INTEGER) AS bucket,
               COUNT(*) AS count
        FROM photos WHERE missing = 0 AND takenAt IS NOT NULL
        GROUP BY bucket ORDER BY bucket""")
    fun hourBuckets(): Flow<List<Bucket>>
}
