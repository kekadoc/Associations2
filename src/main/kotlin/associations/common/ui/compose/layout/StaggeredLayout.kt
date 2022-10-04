package associations.common.ui.compose.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import kotlin.math.max

@Composable
fun StaggeredLayout(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Arrangement.Vertical = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = StaggeredLayoutMeasurePolicy(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment
        )
    )
}


private class StaggeredLayoutMeasurePolicy(
    private val horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    private val verticalAlignment: Arrangement.Vertical = Arrangement.Center
) : MeasurePolicy {
    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
    
        val horizontalArrangementSpacing = horizontalArrangement.spacing.roundToPx()
        val verticalArrangementSpacing = verticalAlignment.spacing.roundToPx()
    
        val placeables = measurables.map { measurable -> measurable.measure(constraints) }
        
        val wrapContentHeight = measureMinHeight(
            constraints = constraints,
            horizontalSpacing = horizontalArrangementSpacing,
            verticalSpacing = verticalArrangementSpacing,
            placeables = placeables
        )
        
        val desiredWidth = constraints.constrainWidth(constraints.maxWidth)
        val desiredHeight = constraints.constrainHeight(wrapContentHeight)
        
        return layout(desiredWidth, desiredHeight) {
            var yPosition = 0
            var xPosition = 0
            var maxHeight = 0
            var index = 0
            placeables.forEach { placeable ->
                val width = placeable.width
                val height = placeable.height
                if (index == 0) {
                    placeable.placeRelative(0, yPosition)
                    maxHeight = max(maxHeight, height)
                    xPosition += width
                    index++
                } else if (xPosition + horizontalArrangementSpacing + width <= desiredWidth) {
                    placeable.placeRelative(xPosition + horizontalArrangementSpacing, yPosition)
                    maxHeight = max(maxHeight, height)
                    xPosition += horizontalArrangementSpacing + width
                    index++
                } else {
                    yPosition += maxHeight + verticalArrangementSpacing
                    maxHeight = 0
                    xPosition = 0
                    index = 0
                    placeable.placeRelative(xPosition, yPosition)
                    xPosition += width
                    index++
                    maxHeight = height
                }
            }
        }
    }
    
    private fun measureMinHeight(
        constraints: Constraints,
        horizontalSpacing: Int,
        verticalSpacing: Int,
        placeables: List<Placeable>
    ): Int {
        var minHeight = 0
        var rowWidth = 0
        var maxItemHeight = 0
        placeables.forEachIndexed { index, placeable ->
            when {
                index == 0 -> {
                    rowWidth += placeable.width
                    maxItemHeight = max(maxItemHeight, placeable.height)
                }
                rowWidth + horizontalSpacing + placeable.width <= constraints.maxWidth -> {
                    rowWidth += horizontalSpacing + placeable.width
                    maxItemHeight = max(maxItemHeight, placeable.height)
                }
                else -> {
                    minHeight += maxItemHeight + verticalSpacing
                    maxItemHeight = placeable.height
                    rowWidth = placeable.width
                }
            }
        }
        minHeight += maxItemHeight
        return minHeight
    }
}