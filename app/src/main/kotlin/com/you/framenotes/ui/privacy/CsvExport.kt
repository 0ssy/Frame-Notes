package com.you.framenotes.ui.privacy

import android.content.Context
import android.net.Uri
import com.you.framenotes.data.PhotoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun writeCsv(ctx: Context, dao: PhotoDao, dest: Uri) = withContext(Dispatchers.IO) {
    fun esc(s: Any?): String {
        val t = s?.toString() ?: ""
        return if (t.any { it == ',' || it == '"' || it == '\n' }) "\"${t.replace("\"", "\"\"")}\"" else t
    }
    ctx.contentResolver.openOutputStream(dest)!!.bufferedWriter().use { w ->
        w.appendLine("name,camera,lens,focal_mm,focal_35mm,aperture,shutter_s,iso,taken_at,lat,lon")
        dao.allForExport().forEach { p ->
            w.appendLine(listOf(
                p.displayName, p.camera, p.lens, p.focalLength, p.focalLength35, p.aperture,
                p.shutterSeconds, p.iso,
                p.takenAt?.let { java.time.Instant.ofEpochMilli(it).toString() }, p.latitude, p.longitude
            ).joinToString(",") { esc(it) })
        }
    }
}
