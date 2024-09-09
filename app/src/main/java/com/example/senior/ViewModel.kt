package com.example.senior.networking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.senior.data.LedRequestBody
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.InputStreamReader

class PostViewModel : ViewModel() {

    private val postApi = PostApi.instance

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

    fun ledArrayOn(context: Context, filename: String) {
        viewModelScope.launch {
            try {
                val ledRequest = getLedRequestBodyFromJson(context, filename)
                postApi.ledArrayOn(ledRequest)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getLedRequestBodyFromJson(context: Context, filename: String): LedRequestBody {
        val inputStream = context.assets.open(filename)
        val reader = InputStreamReader(inputStream)
        return Gson().fromJson(reader, LedRequestBody::class.java)
    }

    private fun loadJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}
