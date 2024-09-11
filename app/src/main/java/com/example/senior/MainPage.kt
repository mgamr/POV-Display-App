package com.example.senior

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LedControlScreen(viewModel: PostViewModel = viewModel()) {
    val context = LocalContext.current

    Column {
        Button(onClick = { viewModel.ledOn() }) {
            Text(text = "LED On")
        }

        Button(onClick = { viewModel.ledOff() }) {
            Text(text = "LED Off")
        }

        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/mario.json") }) {
            Text(text = "Mario")
        }

        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/small_test.json") }) {
            Text(text = "Small Test")
        }

        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/other.json") }) {
            Text(text = "Sin")
        }

        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/empty.json") }) {
            Text(text = "Empty")
        }

//        Button(onClick = {  }) {
//            Text(text = "Choose image")
//        }

//        Button(onClick = {
//            viewModel.convertImageToArray(context)
//            print("Json converted")
//            val jsonFile = File(context.filesDir, "mario.json")
//            viewModel.ledArrayOn(context, jsonFile)
//        }) {
//            Text(text = "Show")
//        }
    }
}

