package screens.about.components

import ContributionGraph
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

private const val DaysInWeek = 7

@Composable
fun ContributionGraph(
    graph: ContributionGraph,
    peakColor: Color,
    emptyColor: Color = peakColor.copy(alpha = 0f),
    modifier: Modifier = Modifier,
    gapFraction: Float = 0.15f
) {
    val grid = remember(graph.days) { ContributionWeekGrid.from(graph.days) }
    if (grid.weekCount == 0) return

    Canvas(modifier.clipToBounds()) {
        val cell = size.height / (DaysInWeek + (DaysInWeek - 1) * gapFraction)
        if (cell <= 0f) return@Canvas
        val gap = cell * gapFraction
        val stride = cell + gap

        val gridWidth = cell * grid.weekCount + gap * (grid.weekCount - 1)
        val gridHeight = cell * DaysInWeek + gap * (DaysInWeek - 1)
        val originX = size.width - gridWidth
        val originY = size.height - gridHeight

        for (dayRow in 0 until DaysInWeek) {
            for (weekCol in 0 until grid.weekCount) {
                val x = originX + weekCol * stride
                val y = originY + dayRow * stride

                if (x + cell <= 0f || x >= size.width ||
                    y + cell <= 0f || y >= size.height
                ) continue

                drawRect(
                    color = contributionColor(
                        count = grid.countAt(weekCol, dayRow),
                        maxCount = grid.maxCount,
                        empty = emptyColor,
                        peak = peakColor,
                    ),
                    topLeft = Offset(x, y),
                    size = Size(cell, cell)
                )
            }
        }
    }
}

private fun contributionColor(
    count: Int,
    maxCount: Int,
    empty: Color,
    peak: Color,
): Color {
    if (count <= 0 || maxCount <= 0) return empty
    return lerp(empty, peak, count.toFloat() / maxCount.toFloat())
}

private data class ContributionWeekGrid(
    val weekCount: Int,
    val maxCount: Int,
    private val counts: List<Int>
) {
    fun countAt(week: Int, dayOfWeek: Int): Int =
        counts[dayOfWeek * weekCount + week]

    companion object {
        fun from(days: Map<String, Int>): ContributionWeekGrid {
            if (days.isEmpty()) {
                return ContributionWeekGrid(weekCount = 0, maxCount = 0, counts = emptyList())
            }

            val sorted = days.keys.map(LocalDate::parse).sorted()
            val first = sorted.first()
            val last = sorted.last()

            val startOffset = first.dayOfWeek.isoDayNumber % DaysInWeek
            val start = first.toEpochDays() - startOffset
            val endOffset = DaysInWeek - 1 - (last.dayOfWeek.isoDayNumber % DaysInWeek)
            val end = last.toEpochDays() + endOffset
            val totalDays = (end - start + 1).toInt()
            val weekCount = totalDays / DaysInWeek

            val counts = MutableList(weekCount * DaysInWeek) { 0 }
            var maxCount = 0
            for ((dateKey, count) in days) {
                val date = LocalDate.parse(dateKey)
                val dayIndex = (date.toEpochDays() - start).toInt()
                if (dayIndex !in 0 until totalDays) continue
                val week = dayIndex / DaysInWeek
                val dayOfWeek = dayIndex % DaysInWeek
                counts[dayOfWeek * weekCount + week] = count
                if (count > maxCount) maxCount = count
            }

            return ContributionWeekGrid(weekCount, maxCount, counts)
        }
    }
}
