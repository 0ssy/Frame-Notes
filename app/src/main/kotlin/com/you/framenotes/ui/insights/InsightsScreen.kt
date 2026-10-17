package com.you.framenotes.ui.insights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.you.framenotes.ui.EmptyState
import com.you.framenotes.ui.chart.BarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(vm: InsightsViewModel = hiltViewModel(), onBack: () -> Unit) {
    val state by vm.state.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Insights") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "back") } }
            )
        }
    ) { p ->
        state?.let { s ->
            if (s.total == 0) {
                EmptyState("No data", "Index photos to see insights.")
                return@Scaffold
            }
            Column(
                Modifier.padding(p).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
            ) {
                Text("${s.total} photos · ${s.cameras} cameras · favorite ${s.topFocal ?: "?"}mm · ${s.goldenSharePct}% near sunrise/sunset", style = MaterialTheme.typography.bodyMedium)
                Text("Focal length distribution", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                BarChart(s.focal)
                Text("ISO distribution", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                BarChart(s.iso)
                Text("Time of day", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                BarChart(s.time)
            }
        } ?: EmptyState("Loading", "")
    }
}
