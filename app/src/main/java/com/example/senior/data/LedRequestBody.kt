package com.example.senior.data

//data class LedRequestBody(val ledArray: String)

//data class LedRequestBody(val ledArray: LedRequestBody)
data class LedRequestBody(
    val ledArray: List<List<Long>>
)