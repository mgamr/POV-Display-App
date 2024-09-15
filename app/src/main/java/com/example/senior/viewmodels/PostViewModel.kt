package com.example.senior.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senior.data.LedMatrix
import com.example.senior.data.LedRequestBody
import com.example.senior.networking.PostApi
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStreamReader

class PostViewModel : ViewModel() {

    private val postApi = PostApi.instance

    private var succsessCount = 0

    fun motorStop() {
        viewModelScope.launch {
            try {
                postApi.motorStop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun ledOn() {
        viewModelScope.launch {
            try {
                postApi.ledOn()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun ledOff() {
        viewModelScope.launch {
            try {
                postApi.ledOff()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun ledArrayOn(context: Context, filename: String) {
//        viewModelScope.launch {
//            try {
//                val ledRequest = getLedRequestBodyFromJson(context, filename)
//                postApi.ledArrayOn(ledRequest)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun sendArrayAsPackets(context: Context, filename: String) {
        val ledRequest = getLedRequestBodyFromJson(context, filename)

        viewModelScope.launch {
            ledRequest.ledMatrix.forEachIndexed { index, row ->
                sendRowWithRetry(row, index)
            }
        }
    }

    fun sendArrayAsPackets(context: Context, ledMatrix: List<List<Int>>) {
        Log.d("packets", ledMatrix.size.toString())

        viewModelScope.launch {
            ledMatrix.mapIndexed { index, row ->
                async { sendRowWithRetry(row, index) }
            }.awaitAll()
        }
    }

    fun sendArrayAsPackets2(context: Context, ledMatrix: List<List<Int>>) {
        viewModelScope.launch {
            sendstartend("start")
            Log.d("blla",ledMatrix.toString())
            ledMatrix.mapIndexed { index, row ->
                async { sendRowWithRetry2(row, index) }
            }.awaitAll()
            sendstartend("end")
            if(succsessCount != 36){
                Log.e("eroor","somthing not sent")
            }else{
                Log.e("eroor","sent")
                succsessCount = 0
            }
        }
    }

    private suspend fun sendstartend(text: String){
        try {
            val response = postApi.start(text)

            if (response.isSuccessful) {
                Log.d("sent",response.message())
            } else {
                println("Error sending row (attempt ) with index : ${response.code()}")
            }

        } catch (e: Exception) {
            println("Exception on attempt  for index : ${e.message}")
        }
    }

    private suspend fun sendRowWithRetry2(row: List<Int>, index: Int) {
        try {
            val response = postApi.ledArrayOn2(row)
        } catch (e: Exception) {
            println("Exception on attempt  for index : ${e.message}")
        }
    }

    private suspend fun sendRowWithRetry(row: List<Int>, index: Int, maxRetries: Int = 3, delayMillis: Long = 10) {
        var attempt = 0
        var success = false

        while (attempt < maxRetries && !success) {
            try {
                val response = postApi.ledArrayOn(LedRequestBody(row, index))

                if (response.isSuccessful) {
                    println("Row sent successfully with index $index: $row")
                    success = true
                } else {
                    println("Error sending row (attempt $attempt) with index $index: ${response.code()}")
                }

            } catch (e: Exception) {
                println("Exception on attempt $attempt for index $index: ${e.message}")
            }

            if (!success) {
                attempt++
                if (attempt < maxRetries) {
                    delay(delayMillis)
                }
            }
        }

        if (!success) {
            println("Failed to send row with index $index after $maxRetries attempts: $row")
        }
    }

//    private fun getLedMatrixFromJson(context: Context, filename: String): LedMatrix {
//        val inputStream = context.assets.open(filename)
//        val reader = InputStreamReader(inputStream)
//        return Gson().fromJson(reader, LedMatrix::class.java)
//    }



//    fun sendArrayAsPackets(context: Context, filename: String) {
//        val ledRequest = getLedRequestBodyFromJson(context, filename)
//
//        for (row in ledRequest.ledArray) {
//            ledArrayOn(row)
//        }
//    }

    private fun getLedRequestBodyFromJson(context: Context, filename: String): LedMatrix {
        val inputStream = context.assets.open(filename)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, LedMatrix::class.java)
    }


//    fun ledArrayOn(context: Context, file: File) {
//        viewModelScope.launch {
//            try {
//                val ledRequest = getLedRequestBodyFromJson(context, file)
//                postApi.ledArrayOn(ledRequest)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//
//    private fun getLedRequestBodyFromJson(context: Context, file: File): LedRequestBody {
//        val inputStream = file.inputStream()
//        val reader = InputStreamReader(inputStream)
//        return Gson().fromJson(reader, LedRequestBody::class.java)
//    }
//
//    fun convertImageToArray(context: Context) {
//        if (! Python.isStarted()) {
//            Python.start(AndroidPlatform(context))
//        }
//
//        val py = Python.getInstance()
//        val module = py.getModule("convert")
//
////        module.callAttr("convert","C:/Users/Mari/Desktop/mario.png")
//        val assetManager = context.assets
//        val imageName = "images/mario.png"  // Replace with your actual image name
//
//        assetManager.open(imageName).use { inputStream ->
//            val file = File(context.filesDir, imageName)
//            file.outputStream().use { fileOut ->
//                inputStream.copyTo(fileOut)
//            }
//
//            // Pass the path of the copied file to the Python script
//            module.callAttr("convert", file.absolutePath)
//        }
//    }
}
