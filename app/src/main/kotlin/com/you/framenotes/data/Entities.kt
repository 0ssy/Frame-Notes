package com.you.framenotes.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "photos",
    indices = [Index("uri", unique = true), Index("camera"), Index("takenAt"), Index("focalLength35")]
)
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val displayName: String,
    val lastModified: Long,
    val sizeBytes: Long,
    val camera: String?,
    val lens: String?,
    val focalLength: Float?,
    val focalLength35: Int?,
    val aperture: Float?,
    val shutterSeconds: Float?,
    val iso: Int?,
    val takenAt: Long?,
    val width: Int?,
    val height: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val missing: Boolean = false,
)

@Entity(
    tableName = "photo_notes",
    foreignKeys = [ForeignKey(PhotoEntity::class, ["id"], ["photoId"], onDelete = ForeignKey.CASCADE)]
)
data class PhotoNoteEntity(
    @PrimaryKey val photoId: Long,
    val text: String,
    val updatedAt: Long,
)

@Entity(tableName = "tags", indices = [Index("name", unique = true)])
data class TagEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String)

@Entity(
    tableName = "photo_tags",
    primaryKeys = ["photoId", "tagId"],
    foreignKeys = [
        ForeignKey(PhotoEntity::class, ["id"], ["photoId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(TagEntity::class, ["id"], ["tagId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("tagId")]
)
data class PhotoTagCrossRef(val photoId: Long, val tagId: Long)

@Entity(tableName = "collections", indices = [Index("name", unique = true)])
data class CollectionEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val name: String)

@Entity(
    tableName = "photo_collections",
    primaryKeys = ["photoId", "collectionId"],
    foreignKeys = [
        ForeignKey(PhotoEntity::class, ["id"], ["photoId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(CollectionEntity::class, ["id"], ["collectionId"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index("collectionId")]
)
data class PhotoCollectionCrossRef(val photoId: Long, val collectionId: Long)
