package com.you.framenotes.ui.privacy

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private val GPS_TAGS = listOf(
    ExifInterface.TAG_GPS_LATITUDE, ExifInterface.TAG_GPS_LATITUDE_REF,
    ExifInterface.TAG_GPS_LONGITUDE, ExifInterface.TAG_GPS_LONGITUDE_REF,
    ExifInterface.TAG_GPS_ALTITUDE, ExifInterface.TAG_GPS_ALTITUDE_REF,
    ExifInterface.TAG_GPS_TIMESTAMP, ExifInterface.TAG_GPS_DATESTAMP,
    ExifInterface.TAG_GPS_PROCESSING_METHOD, ExifInterface.TAG_GPS_AREA_INFORMATION,
)
private val IDENTITY_TAGS = listOf(
    ExifInterface.TAG_BODY_SERIAL_NUMBER, ExifInterface.TAG_LENS_SERIAL_NUMBER,
    ExifInterface.TAG_CAMERA_OWNER_NAME, ExifInterface.TAG_ARTIST,
)

suspend fun exportClean(ctx: Context, src: Uri, name: String, alsoStripIdentity: Boolean): Uri =
    withContext(Dispatchers.IO) {
        val dir = File(ctx.cacheDir, "share").apply { mkdirs() }
        val safe = name.replace(Regex("[^A-Za-z0-9._-]"), "_")
        val out = File(dir, "clean_$safe")
        ctx.contentResolver.openInputStream(src)?.use { i -> out.outputStream().use { o -> i.copyTo(o) } }

        val exif = ExifInterface(out)
        (GPS_TAGS + if (alsoStripIdentity) IDENTITY_TAGS else emptyList())
            .forEach { exif.setAttribute(it, null) }
        exif.saveAttributes()

        FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", out)
    }

fun sharePhoto(ctx: Context, uri: Uri) = ctx.startActivity(
    Intent.createChooser(
        Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }, "Share clean copy"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy export") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "back") } }
            )
        }
    ) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            Text("Strip GPS and optionally identity data, then share a clean copy.")
            Button(onClick = {
                // This is a placeholder; integrate with a selected photo in production
                scope.launch { }
            }) { Text("Export & Share") }
        }
    }
}
