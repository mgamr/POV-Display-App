package com.example.senior.pages

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.model.Point
import com.example.senior.viewmodels.GraphViewModel
import com.example.senior.viewmodels.PostViewModel

@Composable
fun GraphPage(map: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>, graphViewModel: GraphViewModel = viewModel(), postViewModel: PostViewModel = viewModel()) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    val equationsHistory = remember { mutableStateListOf<String>() }
//    val myMatrix = graphViewModel.getPixelFromImage(R.drawable.marioreal)

    val myMatrix = graphViewModel.getPixelFromImage2(map)

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            Log.d("post", myMatrix.toString())
            postViewModel.sendArrayAsPackets2(context, myMatrix) }
        ) {
            Text("Send")
        }
        Box(modifier = Modifier.padding(16.dp)) {
//            LineChartScreen(equationsHistory = equationsHistory, viewModel = graphViewModel, chartLineType = ChartLineType.STRAIGHT)
            val pointsData = graphViewModel.evaluateEquation("y=x+1")
//            LineChartScreen(pointsData = pointsData)
//            LineChartScreen()
            Graph(pointsData)
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
                        text = ""
                    }
                }
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = null
                )
            }
        }

    }
}



// Composable function to draw the graph
@Composable
fun Graph(points: List<Point>, modifier: Modifier = Modifier) {
    // Define the dimensions of the graph
    val graphWidth = 800f
    val graphHeight = 800f
    val padding = 50f

    Canvas(modifier = modifier.size(graphWidth.dp, graphHeight.dp)) {
        // Draw axes
        drawLine(
            color = Color.Black,
            start = Offset(padding, graphHeight - padding),
            end = Offset(graphWidth - padding, graphHeight - padding), // X-axis
            strokeWidth = 5f
        )
        drawLine(
            color = Color.Black,
            start = Offset(padding, graphHeight - padding),
            end = Offset(padding, padding), // Y-axis
            strokeWidth = 5f
        )

        // Calculate scaling factors based on the range of the points
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }
        val scaleX = (graphWidth - 2 * padding) / maxX
        val scaleY = (graphHeight - 2 * padding) / maxY

        // Draw the points and connect them with lines (to form the graph)
        val path = Path()
        points.forEachIndexed { index, point ->
            val x = padding + point.x * scaleX
            val y = graphHeight - (padding + point.y * scaleY) // Flip y-axis for proper orientation

            if (index == 0) {
                // Move to the first point
                path.moveTo(x, y)
            } else {
                // Draw a line to the next point
                path.lineTo(x, y)
            }

            // Draw point as a circle
            drawCircle(
                color = Color.Red,
                radius = 5f,
                center = Offset(x, y)
            )
        }

        // Draw the path connecting the points
        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 3f)
        )
    }
}

//@Composable
//fun ScreenCaptureComposable(context: Context, graphViewModel: GraphViewModel) {
//    val view = LocalView.current
//
//    Button(onClick = {
//        // Capture the screenshot
//        val screenshotBitmap = captureScreenshot(view)
//
//        // Convert to ImageBitmap
//        val imageBitmap = screenshotBitmap.asImageBitmap()
//
//        // Use with graphViewModel
//        val myMatrix = graphViewModel.getPixelFromImage1(imageBitmap, context)
//    }) {
//        Text("Capture Screenshot")
//    }
//}


// needs different Canvas library
//fun captureScreenshot(view: View): Bitmap {
//    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(bitmap)
//    view.draw(canvas)
//    return bitmap
//}


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