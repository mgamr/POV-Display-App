package com.example.senior.pages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.senior.viewmodels.GraphViewModel

enum class ChartLineType {
    STRAIGHT, SMOOTH
}

@Composable
fun LineChartScreen(equationsHistory: List<String>, viewModel: GraphViewModel = viewModel(), chartLineType: ChartLineType) {
    val steps = 11
//    val steps = equationsHistory.size

    val minX = -5f
    val maxX = 5f
    val minY = -50f
    val maxY = 50f

    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .backgroundColor(Color.Transparent)
        .steps(steps - 1)
        .labelData { i -> (-5 + i).toString() }
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps - 1)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
//        .labelData { i ->
//            (-50 + (i * 10)).toString()
//            i.toString()
//            val yScale = maxY / steps
//            (i * yScale).toString()
//        }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()


    // Create a list of Line objects, one for each equation
    val lines = equationsHistory.map { equation ->
        val pointsData = viewModel.evaluateEquation(equation)

        val scaledPoints = pointsData.map { point ->
            val scaledX = (point.x - minX) / (maxX - minX) // Scale X to be between 0 and 1
            val scaledY = (point.y - minY) / (maxY - minY) // Scale Y to be between 0 and 1
            Point(scaledX, scaledY) // Return the scaled point
        }

        // Select the line type based on the passed parameter
        val selectedLineType = when (chartLineType) {
            ChartLineType.STRAIGHT -> LineType.Straight(isDotted = false)
            ChartLineType.SMOOTH -> LineType.SmoothCurve(isDotted = false)
        }

        // Create a Line object for each set of pointsData
        Line(
//            dataPoints = scaledPoints,
            dataPoints = pointsData,
            LineStyle(
                color = MaterialTheme.colorScheme.tertiary, // You can also dynamically set different colors
                lineType = selectedLineType
            ),
            IntersectionPoint(color = MaterialTheme.colorScheme.tertiary),
            SelectionHighlightPoint(color = MaterialTheme.colorScheme.tertiary),
            ShadowUnderLine(
                alpha = 0.5f,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiary,
                        Color.Transparent
                    )
                )
            ),
            SelectionHighlightPopUp()
        )
    }

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = lines
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant)
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )

//    val lineChartData = LineChartData(
//        linePlotData = LinePlotData(
//            lines = listOf(
//                Line(
//                    dataPoints = pointsData,
//                    LineStyle(
//                        color = MaterialTheme.colorScheme.tertiary,
//                        lineType = LineType.Straight(isDotted = false)
//                    ),
//                    IntersectionPoint(color = MaterialTheme.colorScheme.tertiary),
//                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.tertiary),
//                    ShadowUnderLine(
//                        alpha = 0.5f,
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                MaterialTheme.colorScheme.tertiary,
//                                Color.Transparent
//                            )
//                        )
//                    ),
//                    SelectionHighlightPopUp()
//                )
//            )
//        ),
//        backgroundColor =  MaterialTheme.colorScheme.surface,
//        xAxisData = xAxisData,
//        yAxisData = yAxisData,
//        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant)
//    )
//
//    LineChart(
//        modifier = Modifier.fillMaxWidth().height(300.dp),
//        lineChartData = lineChartData
//    )
}
