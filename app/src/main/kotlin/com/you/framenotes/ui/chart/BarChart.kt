package com.you.framenotes.ui.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

data class BarDatum(val label: String, val value: Int)

@Composable
fun BarChart(
    bars: List<BarDatum>,
    modifier: Modifier = Modifier,
) {
    val measurer = rememberTextMeasurer()
    val axisColor = MaterialTheme.colorScheme.onSurfaceVariant
    val barColor = MaterialTheme.colorScheme.primary
    val labelStyle = MaterialTheme.typography.labelSmall.copy(color = axisColor)
    val progress = remember(bars) { Animatable(0f) }
    LaunchedEffect(bars) { progress.animateTo(1f, tween(600)) }

    val summary = bars.joinToString { "${it.label}: ${it.value}" }
    Canvas(
        modifier.fillMaxWidth().height(200.dp)
            .semantics { contentDescription = "Bar chart. $summary" }
    ) {
        if (bars.isEmpty()) return@Canvas
        val maxV = bars.maxOf { it.value }.coerceAtLeast(1)
        val labelH = 22.dp.toPx()
        val chartH = size.height - labelH
        val slot = size.width / bars.size
        val barW = slot * 0.6f

        drawLine(axisColor, Offset(0f, chartH), Offset(size.width, chartH), strokeWidth = 1.dp.toPx())

        bars.forEachIndexed { i, b ->
            val h = chartH * (b.value / maxV.toFloat()) * progress.value
            val x = i * slot + (slot - barW) / 2
            drawRoundRect(
                barColor, topLeft = Offset(x, chartH - h), size = Size(barW, h),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
            val label = measurer.measure(b.label, labelStyle)
            drawText(label, topLeft = Offset(i * slot + (slot - label.size.width) / 2, chartH + 4.dp.toPx()))
        }
    }
}
