package com.you.framenotes.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MockData {
    suspend fun seed(photoDao: PhotoDao, noteDao: NoteDao) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val day = 86400000L
        val hour = 3600000L

        val photos = listOf(
            PhotoEntity(
                uri = "https://picsum.photos/id/10/800/600",
                displayName = "DSC_0102.JPG",
                lastModified = now - day * 1,
                sizeBytes = 4820100,
                camera = "Fujifilm X-T4",
                lens = "XF 35mm f/1.4 R",
                focalLength = 35f,
                focalLength35 = 53,
                aperture = 1.4f,
                shutterSeconds = 0.001f, // 1/1000s
                iso = 100,
                takenAt = now - day * 1 - hour * 5, // 18:00 (golden hour)
                width = 6000,
                height = 4000,
                latitude = 40.7128,
                longitude = -74.0060,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/15/800/600",
                displayName = "DSC_0105.JPG",
                lastModified = now - day * 2,
                sizeBytes = 5120300,
                camera = "Fujifilm X-T4",
                lens = "XF 18-55mm f/2.8-4",
                focalLength = 18f,
                focalLength35 = 27,
                aperture = 2.8f,
                shutterSeconds = 0.002f, // 1/500s
                iso = 400,
                takenAt = now - day * 2 - hour * 11, // 12:00
                width = 6000,
                height = 4000,
                latitude = 35.6762,
                longitude = 139.6503,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/22/800/600",
                displayName = "ILCE_8820.ARW",
                lastModified = now - day * 3,
                sizeBytes = 24100200,
                camera = "Sony A7 IV",
                lens = "FE 24-70mm f/2.8 GM",
                focalLength = 50f,
                focalLength35 = 50,
                aperture = 2.8f,
                shutterSeconds = 0.004f, // 1/250s
                iso = 200,
                takenAt = now - day * 3 - hour * 4, // 19:00
                width = 7008,
                height = 4672,
                latitude = 48.8566,
                longitude = 2.3522,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/28/800/600",
                displayName = "ILCE_8891.ARW",
                lastModified = now - day * 4,
                sizeBytes = 25300100,
                camera = "Sony A7 IV",
                lens = "FE 85mm f/1.4 GM",
                focalLength = 85f,
                focalLength35 = 85,
                aperture = 1.4f,
                shutterSeconds = 0.001f, // 1/1000s
                iso = 100,
                takenAt = now - day * 4 - hour * 13, // 10:00
                width = 7008,
                height = 4672,
                latitude = 37.7749,
                longitude = -122.4194,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/42/800/600",
                displayName = "IMG_3042.CR3",
                lastModified = now - day * 5,
                sizeBytes = 32100400,
                camera = "Canon EOS R5",
                lens = "RF 50mm f/1.2L USM",
                focalLength = 50f,
                focalLength35 = 50,
                aperture = 1.2f,
                shutterSeconds = 0.0005f, // 1/2000s
                iso = 100,
                takenAt = now - day * 5 - hour * 17, // 06:00 (sunrise)
                width = 8192,
                height = 5464,
                latitude = 35.0116,
                longitude = 135.7681,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/64/800/600",
                displayName = "L1000214.DNG",
                lastModified = now - day * 6,
                sizeBytes = 45100000,
                camera = "Leica Q2",
                lens = "Summilux 28mm f/1.7 ASPH",
                focalLength = 28f,
                focalLength35 = 28,
                aperture = 1.7f,
                shutterSeconds = 0.016f, // 1/60s
                iso = 1600,
                takenAt = now - day * 6 - hour * 2, // 21:00 (night)
                width = 8368,
                height = 5584,
                latitude = 40.7128,
                longitude = -74.0060,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/106/800/600",
                displayName = "DSC_0220.JPG",
                lastModified = now - day * 7,
                sizeBytes = 5900000,
                camera = "Fujifilm X-T4",
                lens = "XF 56mm f/1.2 R",
                focalLength = 56f,
                focalLength35 = 85,
                aperture = 1.2f,
                shutterSeconds = 0.002f, // 1/500s
                iso = 200,
                takenAt = now - day * 7 - hour * 6, // 17:00
                width = 6000,
                height = 4000,
                latitude = 35.6762,
                longitude = 139.6503,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/122/800/600",
                displayName = "ILCE_9102.ARW",
                lastModified = now - day * 8,
                sizeBytes = 26000000,
                camera = "Sony A7 IV",
                lens = "FE 135mm f/1.8 GM",
                focalLength = 135f,
                focalLength35 = 135,
                aperture = 1.8f,
                shutterSeconds = 0.001f,
                iso = 800,
                takenAt = now - day * 8 - hour * 10, // 13:00
                width = 7008,
                height = 4672,
                latitude = 48.8566,
                longitude = 2.3522,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/180/800/600",
                displayName = "DSC_5501.NEF",
                lastModified = now - day * 9,
                sizeBytes = 28000000,
                camera = "Nikon Z6 II",
                lens = "NIKKOR Z 24-70mm f/4 S",
                focalLength = 35f,
                focalLength35 = 35,
                aperture = 4.0f,
                shutterSeconds = 0.008f, // 1/125s
                iso = 3200,
                takenAt = now - day * 9 - hour * 1, // 22:00
                width = 6048,
                height = 4024,
                latitude = 37.7749,
                longitude = -122.4194,
            ),
            PhotoEntity(
                uri = "https://picsum.photos/id/250/800/600",
                displayName = "IMG_4102.CR3",
                lastModified = now - day * 10,
                sizeBytes = 31000000,
                camera = "Canon EOS R5",
                lens = "RF 15-35mm f/2.8L IS",
                focalLength = 15f,
                focalLength35 = 15,
                aperture = 8.0f,
                shutterSeconds = 0.016f,
                iso = 100,
                takenAt = now - day * 10 - hour * 5, // 18:00
                width = 8192,
                height = 5464,
                latitude = 35.0116,
                longitude = 135.7681,
            )
        )

        photoDao.upsert(photos)

        val inserted = photoDao.allForExport()
        inserted.forEachIndexed { i, photo ->
            when (i % 4) {
                0 -> {
                    noteDao.saveNote(PhotoNoteEntity(photo.id, "Soft golden hour light on street architecture.", System.currentTimeMillis()))
                    noteDao.addTag(photo.id, "street")
                    noteDao.addTag(photo.id, "golden-hour")
                }
                1 -> {
                    noteDao.saveNote(PhotoNoteEntity(photo.id, "High contrast portrait shot with prime lens.", System.currentTimeMillis()))
                    noteDao.addTag(photo.id, "portrait")
                }
                2 -> {
                    noteDao.saveNote(PhotoNoteEntity(photo.id, "Wide-angle landscape view with great depth of field.", System.currentTimeMillis()))
                    noteDao.addTag(photo.id, "landscape")
                    noteDao.addTag(photo.id, "travel")
                }
                3 -> {
                    noteDao.saveNote(PhotoNoteEntity(photo.id, "Night atmospheric urban snapshot.", System.currentTimeMillis()))
                    noteDao.addTag(photo.id, "night")
                    noteDao.addTag(photo.id, "architecture")
                }
            }
        }
    }
}
