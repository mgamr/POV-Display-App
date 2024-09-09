package com.example.senior

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.senior.networking.PostViewModel


//@Composable
//fun MainPage(modifier: Modifier = Modifier){
//    Button(onClick = { sendData() }) {
//        Text("Sin")
//    }
//}


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

        Button(onClick = {
            viewModel.ledArrayOn(context, "images/empty.json")
            viewModel.ledArrayOn(context, "images/mario.json")
        }) {
            Text(text = "Mario")
        }

        Button(onClick = { viewModel.ledArrayOn(context, "images/small_test.json") }) {
            Text(text = "Small Test")
        }

        Button(onClick = {
            viewModel.ledArrayOn(context, "images/empty.json")
            viewModel.ledArrayOn(context, "images/other.json")
        }) {
            Text(text = "Other")
        }

        Button(onClick = { viewModel.ledArrayOn(context, "images/empty.json") }) {
            Text(text = "Empty")
        }
    }
}
