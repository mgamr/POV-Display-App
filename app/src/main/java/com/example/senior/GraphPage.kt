package com.example.senior

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.senior.ui.theme.SeniorTheme

//val equationsHistory: MutableList<String> = mutableListOf()

@Composable
fun GraphPage(viewModel: PostViewModel = viewModel()) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

    val equationsHistory = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
//            LineChartScreen()
            GetPixelFromImage()
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


@Composable
fun GetPixelFromImage() {
    // Load the image from resources
    val imageBitmap = ImageBitmap.imageResource(id = R.drawable.mario)

    // Access the pixels using PixelMap
    val pixelMap = imageBitmap.toPixelMap()

    // Coordinates of the pixel you want to get
    val x = 100
    val y = 100

    // Get the color of the specific pixel
    val pixelColor = pixelMap[x, y]

    Log.d("blaaa", pixelColor.toString())
//    print("blaaaa")
//    print(pixelColor)
    // Use the pixel color (e.g., display it or log it)
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color = pixelColor)
    )
}

@Preview(showBackground = true)
@Composable
fun GraphPagePreview() {
    SeniorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GraphPage()
        }
    }
}