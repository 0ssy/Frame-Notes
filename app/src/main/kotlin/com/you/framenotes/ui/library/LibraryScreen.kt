package com.you.framenotes.ui.library

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.you.framenotes.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(vm: LibraryViewModel = hiltViewModel(), onOpen: (Long) -> Unit, onInsights: () -> Unit = {}, onPrivacy: () -> Unit = {}) {
    val ctx = LocalContext.current
    val filter by vm.filter.collectAsState()
    val items = vm.photos.collectAsLazyPagingItems()
    val cameras by vm.cameras.collectAsState()

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri != null) {
            ctx.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            vm.onFolderChosen(uri)
        }
    }

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = { Text("Frame Notes") },
            actions = {
                IconButton(onClick = { vm.loadMockData() }) { Icon(Icons.Default.AutoAwesome, "load mock data") }
                IconButton(onClick = onInsights) { Icon(Icons.Default.BarChart, "insights") }
                IconButton(onClick = onPrivacy) { Icon(Icons.Default.Security, "privacy") }
            }
        )

        Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            if (!vm.hasFolder()) {
                Button(onClick = { picker.launch(null) }) { Text("Choose photo folder") }
                Spacer(Modifier.width(8.dp))
            }
            OutlinedButton(onClick = { vm.loadMockData() }) { Text("Load Mock Data") }
        }

        Row(Modifier.fillMaxWidth().padding(8.dp)) {
            OutlinedTextField(
                value = filter.query,
                onValueChange = { q -> vm.update { it.copy(query = q) } },
                label = { Text("Search") },
                modifier = Modifier.weight(1f)
            )
        }

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(cameras.size) { i ->
                val cam = cameras[i]
                FilterChip(
                    selected = filter.camera == cam,
                    onClick = { vm.update { f -> f.copy(camera = if (f.camera == cam) null else cam) } },
                    label = { Text(cam) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }

        if (items.loadState.refresh is LoadState.NotLoading && items.itemCount == 0) {
            EmptyState("No photos match", "Choose a folder or clear filters.")
        } else {
            LazyVerticalGrid(columns = GridCells.Adaptive(110.dp), contentPadding = PaddingValues(4.dp)) {
                items(items.itemCount, key = { i -> items[i]?.id ?: i }) { i ->
                    items[i]?.let { p ->
                        AsyncImage(
                            model = ImageRequest.Builder(ctx).data(Uri.parse(p.uri)).size(300).crossfade(true).build(),
                            contentDescription = p.displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.padding(2.dp).aspectRatio(1f)
                                .clip(RoundedCornerShape(6.dp)).clickable { onOpen(p.id) }
                        )
                    }
                }
            }
        }
    }
}
