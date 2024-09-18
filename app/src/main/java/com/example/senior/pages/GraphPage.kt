package com.example.senior.pages

import android.graphics.Bitmap
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toPixelMap
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
fun GraphPage(map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>, graphViewModel: GraphViewModel = viewModel(), postViewModel: PostViewModel = viewModel()) {
    var text by remember { mutableStateOf("") }

    val equationsHistory = remember { mutableStateListOf<String>() }

//    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.marioreal)
//    val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap)

    var pointsData by remember { mutableStateOf<List<Point>>(listOf()) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Button(onClick = {
//            Log.d("post", myMatrix.toString())
//            postViewModel.sendArrayAsPackets(context, myMatrix) }
//        ) {
//            Text("Send")
//        }
        Box(
            modifier = Modifier
                .padding(10.dp)
                .size(300.dp, 300.dp)
        ) {
//            LineChartScreen(equationsHistory = equationsHistory, viewModel = graphViewModel, chartLineType = ChartLineType.STRAIGHT)
//            val pointsData = graphViewModel.evaluateEquation("y=x+1")
//            LineChartScreen(pointsData = pointsData)
//            LineChartScreen()
            Graph(pointsData, map, graphViewModel, postViewModel)
        }

        LazyColumn (
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(equationsHistory) { equation ->
                Text(text = equation)
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
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
                        text = ""
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

@Composable
fun Graph(
    points: List<Point>,
    map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>,
    graphViewModel: GraphViewModel = viewModel(),
    postViewModel: PostViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Define the dimensions of the graph
    val graphWidth = 800f
    val graphHeight = 800f
    val padding = 50f

    // Define the target dimensions for the resized bitmap
    val targetWidth = 432
    val targetHeight = 432

    // Create the paths for Compose and Android
    val composePath = Path()
    val androidPath = android.graphics.Path()

    // Variables for axes and scaling
    var paddingX = padding
    var paddingY = padding
    var scaleX = 1f
    var scaleY = 1f
    var maxX = 0f
    var minX = 0f
    var maxY = 0f
    var minY = 0f

    // Create a mutable state to hold the ImageBitmap
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Ensure points are available before trying to draw
    if (points.isNotEmpty()) {
        LaunchedEffect(points) {
            // Create a bitmap to draw the graph
            val bitmap = Bitmap.createBitmap(
                graphWidth.toInt(),
                graphHeight.toInt(),
                Bitmap.Config.ARGB_8888
            )

            // Create an Android Canvas using the Bitmap
            val canvas = android.graphics.Canvas(bitmap)

            // Set up Paint for Android Canvas drawing
            val paint = Paint().apply {
                isAntiAlias = true
                color = android.graphics.Color.CYAN
                strokeWidth = 20f
                style = Paint.Style.STROKE
            }

            // Calculate scales for X and Y axes based on points
            maxX = points.maxOf { it.x }
            minX = points.minOf { it.x }
            minY = points.minOf { it.y }
            maxY = points.maxOf { it.y }

            scaleX = (graphWidth - 2 * padding) / (maxX - minX)
            scaleY = (graphHeight - 2 * padding) / (maxY - minY)

            paddingX = padding - minX * scaleX
            paddingY = padding - minY * scaleY

            // Create the path for both Compose and Android Canvas
            points.forEachIndexed { index, point ->
                val x = paddingX + point.x * scaleX
                val y = graphHeight - (paddingY + point.y * scaleY) // Flip y-axis

                if (index == 0) {
                    // Move to the first point in both paths
                    composePath.moveTo(x, y)
                    androidPath.moveTo(x, y)
                } else {
                    // Line to the next point in both paths
                    composePath.lineTo(x, y)
                    androidPath.lineTo(x, y)
                }
            }

            // Draw the graph path on Android's Canvas
            canvas.drawPath(androidPath, paint)

            // Draw the X-axis and Y-axis on the Android Canvas
            paint.apply {
                color = android.graphics.Color.RED
                strokeWidth = 20f
            }
            canvas.drawLine(padding - 50, graphHeight - paddingY, graphWidth - padding + 50, graphHeight - paddingY, paint) // X-axis
            canvas.drawLine(paddingX, graphHeight - padding + 50, paddingX, padding - 50, paint) // Y-axis

            // Draw axis labels on the Android Canvas
            paint.apply {
                textSize = 70f
                style = Paint.Style.FILL
                typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            }
            canvas.drawText("${maxY.roundToInt()}", paddingX + 25f, padding + 25f, paint)  // Top-left Y label
            canvas.drawText("${minY.roundToInt()}", paddingX + 25f, graphWidth - padding, paint)  // Bottom-left Y label
            canvas.drawText("${minX.roundToInt()}", padding - 50f, graphHeight - paddingY - 30f, paint)  // Bottom-left X label
            canvas.drawText("${maxX.roundToInt()}", graphHeight - padding - 25f, graphHeight - paddingY - 30f, paint)  // Bottom-right X label

            // Resize the Bitmap to 432x432
            val resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)

            // Convert the resized Bitmap to an ImageBitmap
            imageBitmap = resizedBitmap.asImageBitmap()
        }

        // Drawing the Compose Path in Jetpack Compose Canvas
        Canvas(modifier = modifier.size(graphWidth.dp, graphHeight.dp)) {
            drawPath(
                path = composePath,
                color = Color.Cyan,
                style = Stroke(width = 20f)
            )
        }

        val context = LocalContext.current
        // Display the generated ImageBitmap
        imageBitmap?.let {
            Image(bitmap = it, contentDescription = "Graph Image", modifier = modifier.size(targetWidth.dp, targetHeight.dp))

            val myMatrix = graphViewModel.getPixelFromImage(map, imageBitmap!!)
            postViewModel.sendArrayAsPackets(context, myMatrix)
            Log.d("screenshot", myMatrix.toString())
            Log.d("screenshot", imageBitmap!!.toPixelMap()[100,100].toString())
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