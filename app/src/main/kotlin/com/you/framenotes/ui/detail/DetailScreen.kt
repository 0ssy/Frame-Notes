package com.you.framenotes.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import android.net.Uri
import com.you.framenotes.ui.MetaRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(vm: DetailViewModel = hiltViewModel(), onBack: () -> Unit) {
    val photo by vm.photo.collectAsState()
    val note by vm.note.collectAsState()
    val tags by vm.tags.collectAsState()
    val ctx = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(photo?.displayName ?: "Detail") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "back") } }
            )
        }
    ) { p ->
        Column(
            Modifier.padding(p).verticalScroll(rememberScrollState())
        ) {
            photo?.let { ph ->
                AsyncImage(
                    model = ImageRequest.Builder(ctx).data(Uri.parse(ph.uri)).crossfade(true).build(),
                    contentDescription = ph.displayName,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                Column(Modifier.padding(16.dp)) {
                    Text("Camera", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    MetaRow("Camera", ph.camera)
                    MetaRow("Lens", ph.lens)
                    Text("Exposure", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    MetaRow("Focal length", com.you.framenotes.exif.ExifParser.formatFocal(ph.focalLength))
                    MetaRow("35mm equiv", ph.focalLength35?.let { "${it}mm" })
                    MetaRow("Aperture", com.you.framenotes.exif.ExifParser.formatAperture(ph.aperture))
                    MetaRow("Shutter", com.you.framenotes.exif.ExifParser.formatShutter(ph.shutterSeconds))
                    MetaRow("ISO", ph.iso?.let { "$it" })
                    Text("Location", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    if (ph.latitude != null && ph.longitude != null) {
                        MetaRow("Lat/Lon", "${ph.latitude}, ${ph.longitude}")
                    } else {
                        MetaRow("Location", "No location data")
                    }
                    Text("File", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                    MetaRow("Name", ph.displayName)
                    MetaRow("Size", ph.sizeBytes.let { "$it bytes" })
                    MetaRow("Dimensions", if (ph.width != null && ph.height != null) "${ph.width} × ${ph.height}" else null)
                }
            }
        }
    }
}
