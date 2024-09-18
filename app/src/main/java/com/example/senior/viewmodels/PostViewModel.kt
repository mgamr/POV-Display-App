package com.example.senior.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senior.networking.PostApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val postApi = PostApi.instance

    private var successCount = 0

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

    fun sendArrayAsPackets(context: Context, ledMatrix: List<List<Int>>) {
        viewModelScope.launch {
            sendStartEnd("start")
            Log.d("bla",ledMatrix.toString())
            ledMatrix.mapIndexed { index, row ->
                async { sendRowWithRetry(row, index) }
            }.awaitAll()
            sendStartEnd("end")
            if(successCount != 36){
                Log.e("eroor","somthing not sent")
            }else{
                Log.e("eroor","sent")
                successCount = 0
            }
        }
    }

    private suspend fun sendStartEnd(text: String){
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

    private suspend fun sendRowWithRetry(row: List<Int>, index: Int) {
        try {
            val response = postApi.ledArrayOn(row)
        } catch (e: Exception) {
            println("Exception on attempt  for index : ${e.message}")
        }
    }
}
