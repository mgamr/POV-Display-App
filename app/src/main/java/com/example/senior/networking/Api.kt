package com.example.senior.networking

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostApi {
    val json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(Constants.PostAPI.URL)
//            .addConverterFactory(
//                json.asConverterFactory("application/json; charset=UTF8".toMediaTypeOrNull()!!)
//            )
//            .build()
//    }

//    val instance: PostService by lazy {
//        retrofit.create(PostService::class.java)
//    }

    val instance: PostService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.PostAPI.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostService::class.java)
    }
}