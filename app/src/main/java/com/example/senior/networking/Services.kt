package com.example.senior.networking

import com.example.senior.data.LedRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface PostService {
//    @POST("/posts")
//    suspend fun post(@Body post: LedMatrix): Response<LedMatrix>

    @POST("/ledOff")
    suspend fun ledOff(): Unit

    @POST("/ledOn")
    suspend fun ledOn(): Unit

//    @Headers("Content-Type: application/json")
//    @POST("/ledArray")
//    suspend fun ledArrayOn(@Body body: LedRequestBody): Unit

    @Headers("Content-Type: application/json")
    @POST("/ledArray")
    suspend fun ledArrayOn(@Body body: LedRequestBody): Response<Unit>
}