package com.example.senior.networking

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

//    @Headers("Content-Type: application/json")
//    @POST("/ledArray")
//    suspend fun ledArrayOn(@Body body: LedRequestBody): Response<Unit>

    @POST("/motorStop")
    suspend fun motorStop(): Unit

    @FormUrlEncoded
    @POST("/start-end")
    suspend fun start(
        @Field("started") col: String
    ): Response<Unit>

    @Headers("Content-Type: text/plain")
    @POST("/post-data")
    suspend fun ledArrayOn(@Body body: List<Int>): Response<Unit>
}