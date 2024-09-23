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
            ledMatrix.mapIndexed { _, row ->
                Log.d("row", row.toString())
                async { sendRowWithRetry(row) }
            }.awaitAll()
            sendStartEnd("end")
        }
    }

    fun sendArrayAsPacketsWithoutStop(context: Context, ledMatrix: List<List<Int>>) {
        viewModelScope.launch {
            sendStartEnd("start_without_stop")
            ledMatrix.mapIndexed { _, row ->
                Log.d("row", row.toString())
                async { sendRowWithRetry(row) }
            }.awaitAll()
            sendStartEnd("end")
        }
    }

    private suspend fun sendStartEnd(text: String) {
        try {
            val response = postApi.start(text)

            if (response.isSuccessful) {
                Log.d("sent", response.message())
            } else {
                println("Error sending row (attempt ) with index : ${response.code()}")
            }

        } catch (e: Exception) {
            println("Exception on attempt  for index : ${e.message}")
        }
    }

    private suspend fun sendRowWithRetry(row: List<Int>) {
        try {
            val response = postApi.ledArrayOn(row)
        } catch (e: Exception) {
            println("Exception on attempt  for index : ${e.message}")
        }
    }

    private fun intToBytes(matrix: List<List<Int>>): List<List<Int>> {
        val res: MutableList<MutableList<Int>> = mutableListOf()
        for (list in matrix) {
            val row: MutableList<Int> = mutableListOf()
            for (value in list) {
                row.add(((value shr 16) and 0xFF))
                row.add(((value shr 8) and 0xFF))
                row.add((value and 0xFF))
            }
            res.add(row)
            Log.d("row", row.toString())
        }
        return res
    }

    fun sendCheckBox(s: String) {
        viewModelScope.launch {
            try {
                postApi.sendCheckBox(s)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
