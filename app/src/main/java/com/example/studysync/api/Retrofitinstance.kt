package com.example.studysync.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofitinstance {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://studysyncmern.azurewebsites.net")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val apiInterface by lazy{
        retrofit.create(ApiInterface::class.java)
    }
}