package com.you.framenotes.ui.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.you.framenotes.data.Bucket
import com.you.framenotes.data.PhotoDao
import com.you.framenotes.ui.chart.BarDatum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.math.roundToInt

data class Insights(
    val total: Int, val cameras: Int, val topFocal: Int?,
    val focal: List<BarDatum>, val iso: List<BarDatum>, val time: List<BarDatum>,
    val goldenSharePct: Int,
)

@HiltViewModel
class InsightsViewModel @Inject constructor(dao: PhotoDao) : ViewModel() {
    private val focalLabels = listOf("<24", "24–34", "35–49", "50–84", "85–134", "135+")
    private val isoLabels = listOf("≤100", "200", "400", "800", "1600", "3200", "6400+")
    private val timeLabels = listOf("00", "03", "06", "09", "12", "15", "18", "21")

    private fun fill(labels: List<String>, rows: List<Bucket>) =
        labels.mapIndexed { i, l -> BarDatum(l, rows.firstOrNull { it.bucket == i }?.count ?: 0) }

    private val summary = combine(dao.total(), dao.cameraCount(), dao.topFocal(), ::Triple)
    private val charts = combine(dao.focalBuckets(), dao.isoBuckets(), dao.hourBuckets(), ::Triple)

    val state = combine(summary, charts) { (total, cams, top), (focal, iso, hours) ->
        val byHour = IntArray(24).also { a -> hours.forEach { if (it.bucket in 0..23) a[it.bucket] = it.count } }
        val timed = byHour.sum()
        val golden = byHour[6] + byHour[7] + byHour[18] + byHour[19]
        Insights(
            total, cams, top,
            fill(focalLabels, focal), fill(isoLabels, iso),
            timeLabels.mapIndexed { i, l -> BarDatum(l, (0..2).sumOf { byHour[i * 3 + it] }) },
            if (timed == 0) 0 else (golden * 100f / timed).roundToInt(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
