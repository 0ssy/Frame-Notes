package com.you.framenotes.exif

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.DocumentsContract.Document
import androidx.exifinterface.media.ExifInterface
import com.you.framenotes.data.PhotoEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

object ExifParser {
    private val DATE_FMT = java.time.format.DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss")

    fun rational(s: String?): Double? {
        val t = s?.trim().takeUnless { it.isNullOrEmpty() } ?: return null
        val p = t.split('/')
        return when (p.size) {
            1 -> p[0].toDoubleOrNull()
            2 -> {
                val n = p[0].toDoubleOrNull(); val d = p[1].toDoubleOrNull()
                if (n != null && d != null && d != 0.0) n / d else null
            }
            else -> null
        }
    }

    fun dateTaken(s: String?): Long? = try {
        s?.trim()?.let {
            java.time.LocalDateTime.parse(it, DATE_FMT).toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
        }
    } catch (_: Exception) { null }

    fun camera(make: String?, model: String?): String? {
        val mk = make?.trim().orEmpty(); val md = model?.trim().orEmpty()
        if (md.isEmpty()) return mk.ifEmpty { null }
        return if (mk.isNotEmpty() && !md.startsWith(mk, ignoreCase = true)) "$mk $md" else md
    }

    fun formatShutter(sec: Float?): String? = when {
        sec == null || sec <= 0f -> null
        sec >= 1f -> "${"%.1f".format(sec).removeSuffix(".0")}s"
        else -> "1/${(1f / sec).roundToInt()}s"
    }

    fun formatAperture(f: Float?): String? = f?.let { "f/${"%.1f".format(it)}" }
    fun formatFocal(mm: Float?): String? = mm?.let { "${it.roundToInt()}mm" }
}

data class DocEntry(
    val uri: Uri, val name: String, val lastModified: Long, val size: Long
)

@Singleton
class ExifReader @Inject constructor(@ApplicationContext private val ctx: Context) {

    fun read(entry: DocEntry, existingId: Long = 0): PhotoEntity? = try {
        ctx.contentResolver.openInputStream(entry.uri)?.use { stream ->
            val e = ExifInterface(stream)
            val ll = e.latLong
            val focal = e.getAttributeDouble(ExifInterface.TAG_FOCAL_LENGTH, -1.0)
            val focal35 = e.getAttributeInt(ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM, -1)
            val ap = e.getAttributeDouble(ExifInterface.TAG_F_NUMBER, -1.0)
            val exp = e.getAttributeDouble(ExifInterface.TAG_EXPOSURE_TIME, -1.0)
            val iso = e.getAttributeInt(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY, -1)
            val w = e.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
            val h = e.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

            PhotoEntity(
                id = existingId,
                uri = entry.uri.toString(),
                displayName = entry.name,
                lastModified = entry.lastModified,
                sizeBytes = entry.size,
                camera = ExifParser.camera(
                    e.getAttribute(ExifInterface.TAG_MAKE), e.getAttribute(ExifInterface.TAG_MODEL)
                ),
                lens = e.getAttribute(ExifInterface.TAG_LENS_MODEL)?.trim()?.ifEmpty { null },
                focalLength = focal.takeIf { it > 0 }?.toFloat(),
                focalLength35 = focal35.takeIf { it > 0 },
                aperture = ap.takeIf { it > 0 }?.toFloat(),
                shutterSeconds = exp.takeIf { it > 0 }?.toFloat(),
                iso = iso.takeIf { it > 0 },
                takenAt = ExifParser.dateTaken(
                    e.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                        ?: e.getAttribute(ExifInterface.TAG_DATETIME)
                ),
                width = w.takeIf { it > 0 },
                height = h.takeIf { it > 0 },
                latitude = ll?.get(0),
                longitude = ll?.get(1),
            )
        }
    } catch (_: IOException) { null }
}
