package com.example.senior.pages

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.model.Point
import com.example.senior.ui.theme.Pink
import com.example.senior.viewmodels.GraphViewModel
import com.example.senior.viewmodels.PostViewModel
import kotlin.math.roundToInt

@Composable
fun GraphPage(
    map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>,
    graphViewModel: GraphViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel()
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    val equationsHistory = remember { mutableStateListOf<String>() }
    var pointsData by remember { mutableStateOf<List<Point>>(listOf()) }

    var start by remember { mutableFloatStateOf(-10f) }
    var end by remember { mutableFloatStateOf(10f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .size(300.dp, 300.dp)
        ) {
//            LineChartScreen(equationsHistory = equationsHistory, viewModel = graphViewModel, chartLineType = ChartLineType.STRAIGHT)
//            val pointsData = graphViewModel.evaluateEquation("y=x+1")
//            LineChartScreen(pointsData = pointsData)
//            LineChartScreen()
            Graph(pointsData, map, graphViewModel)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    start -= 1f
                    end -= 1f
                    pointsData = graphViewModel.evaluateEquation(
                        equationsHistory.lastOrNull() ?: "",
                        start,
                        end
                    )

                    val imageBitmap = generateGraphBitmap(pointsData)

                    val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap)
                    postViewModel.sendArrayAsPacketsWithoutStop(context, myMatrix)
                },
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = {
                    start -= 1f
                    end += 1f
                    pointsData = graphViewModel.evaluateEquation(
                        equationsHistory.lastOrNull() ?: "",
                        start,
                        end
                    )

                    val imageBitmap = generateGraphBitmap(pointsData)
                    val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap)
                    postViewModel.sendArrayAsPacketsWithoutStop(context, myMatrix)
                },
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Text(text = "Zoom Out", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = {
                    start += 1f
                    end -= 1f
                    pointsData = graphViewModel.evaluateEquation(
                        equationsHistory.lastOrNull() ?: "",
                        start,
                        end
                    )

                    val imageBitmap = generateGraphBitmap(pointsData)

                    val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap)
                    postViewModel.sendArrayAsPacketsWithoutStop(context, myMatrix)
                },
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Text(text = "Zoom In", color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = {
                    start += 1f
                    end += 1f
                    pointsData = graphViewModel.evaluateEquation(
                        equationsHistory.lastOrNull() ?: "",
                        start,
                        end
                    )

                    val imageBitmap = generateGraphBitmap(pointsData)

                    val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap)
                    postViewModel.sendArrayAsPacketsWithoutStop(context, myMatrix)
                },
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(equationsHistory) { equation ->
                Text(text = equation)
            }
        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Enter equation") }
            )
            Button(
                modifier = Modifier.padding(start = 8.dp),
                onClick = {
                    if (text.isNotEmpty()) {
                        equationsHistory.add(text)
                        pointsData = graphViewModel.evaluateEquation(text)

                        val imageBitmap = generateGraphBitmap(pointsData)

                        val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap)
                        postViewModel.sendArrayAsPackets(context, myMatrix)

                        text = ""
                        start = -10f
                        end = 10f
                    }
                },
                colors = ButtonDefaults.buttonColors(Pink)
            ) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = null,
                    tint = Color.DarkGray
                )
            }
        }

    }
}

fun generateGraphBitmap(points: List<Point>): ImageBitmap {
    val graphWidth = 800f
    val graphHeight = 800f
    val padding = 50f

    val targetWidth = 432
    val targetHeight = 432

    val bitmap = Bitmap.createBitmap(
        graphWidth.toInt(),
        graphHeight.toInt(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        color = android.graphics.Color.CYAN
        strokeWidth = 20f
        style = Paint.Style.STROKE
    }

    val maxX = points.maxOf { it.x }
    val minX = points.minOf { it.x }
    val minY = points.minOf { it.y }
    val maxY = points.maxOf { it.y }

    val scaleX = (graphWidth - 2 * padding) / (maxX - minX)
    val scaleY = (graphHeight - 2 * padding) / (maxY - minY)

    val paddingX = padding - minX * scaleX
    val paddingY = padding - minY * scaleY

    val androidPath = android.graphics.Path()

    points.forEachIndexed { index, point ->
        val x = paddingX + point.x * scaleX
        val y = graphHeight - (paddingY + point.y * scaleY) // Flip y-axis

        if (index == 0) {
            androidPath.moveTo(x, y)
        } else {
            androidPath.lineTo(x, y)
        }
    }

    canvas.drawPath(androidPath, paint)

    paint.apply {
        color = android.graphics.Color.RED
        strokeWidth = 20f
    }
    canvas.drawLine(
        padding - 50,
        graphHeight - paddingY,
        graphWidth - padding + 50,
        graphHeight - paddingY,
        paint
    ) // X-axis
    canvas.drawLine(paddingX, graphHeight - padding + 50, paddingX, padding - 50, paint) // Y-axis

    paint.apply {
        textSize = 70f
        style = Paint.Style.FILL
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    canvas.drawText(
        "${maxY.roundToInt()}",
        paddingX + 25f,
        padding + 25f,
        paint
    )  // Top-left Y label
    canvas.drawText(
        "${minY.roundToInt()}",
        paddingX + 25f,
        graphWidth - padding,
        paint
    )  // Bottom-left Y label
    canvas.drawText(
        "${minX.roundToInt()}",
        padding - 50f,
        graphHeight - paddingY - 30f,
        paint
    )  // Bottom-left X label
    canvas.drawText(
        "${maxX.roundToInt()}",
        graphHeight - padding - 25f,
        graphHeight - paddingY - 30f,
        paint
    )  // Bottom-right X label

    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)

    return resizedBitmap.asImageBitmap()
}


@Composable
fun Graph(
    points: List<Point>,
    map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>,
    graphViewModel: GraphViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val graphWidth = 800f
    val graphHeight = 800f
    val padding = 50f

    val composePath = Path()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    if (points.isNotEmpty()) {
        LaunchedEffect(points) {
            imageBitmap = generateGraphBitmap(points)
        }

        imageBitmap?.let {
            Image(
                bitmap = it,
                contentDescription = "Graph Image",
                modifier = modifier.size(432.dp, 432.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GraphPagePreview() {
//    SeniorTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            GraphPage()
//        }
//    }
}