package com.example.senior.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostApi {
    val instance: PostService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.PostAPI.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostService::class.java)
    }
}