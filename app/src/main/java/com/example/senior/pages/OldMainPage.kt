package com.example.senior.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.senior.viewmodels.PostViewModel


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

//        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/mario.json") }) {
//            Text(text = "Mario")
//        }
//
//        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/small_test.json") }) {
//            Text(text = "Small Test")
//        }
//
//        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/other.json") }) {
//            Text(text = "Sin")
//        }
//
//        Button(onClick = { viewModel.sendArrayAsPackets(context, "jsons/empty.json") }) {
//            Text(text = "Empty")
//        }

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



//fun sendArrayAsPackets(context: Context, filename: String) {
//    val ledRequest = getLedRequestBodyFromJson(context, filename)
//
//    viewModelScope.launch {
//        ledRequest.ledMatrix.forEachIndexed { index, row ->
//            sendRowWithRetry(row, index)
//        }
//    }
//}

//private fun getLedRequestBodyFromJson(context: Context, filename: String): LedMatrix {
//    val inputStream = context.assets.open(filename)
//    val reader = InputStreamReader(inputStream)
//    return Gson().fromJson(reader, LedMatrix::class.java)
//}

//private suspend fun sendRowWithRetry(row: List<Int>, index: Int, maxRetries: Int = 3, delayMillis: Long = 10) {
//    var attempt = 0
//    var success = false
//
//    while (attempt < maxRetries && !success) {
//        try {
//            val response = postApi.ledArrayOn(LedRequestBody(row, index))
//
//            if (response.isSuccessful) {
//                println("Row sent successfully with index $index: $row")
//                success = true
//            } else {
//                println("Error sending row (attempt $attempt) with index $index: ${response.code()}")
//            }
//
//        } catch (e: Exception) {
//            println("Exception on attempt $attempt for index $index: ${e.message}")
//        }
//
//        if (!success) {
//            attempt++
//            if (attempt < maxRetries) {
//                delay(delayMillis)
//            }
//        }
//    }
//
//    if (!success) {
//        println("Failed to send row with index $index after $maxRetries attempts: $row")
//    }
//}
