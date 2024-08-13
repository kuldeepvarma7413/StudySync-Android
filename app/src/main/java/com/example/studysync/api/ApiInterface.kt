package com.example.studysync.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers(
        "Accept: application/json"
    )
    @POST("auth/login")
    fun login(@Body loginBody: Map<String, String>): Call<ResponseBody>

    @POST("auth/register")
    fun signup(@Body requestBody: Map<String, String>): Call<ResponseBody>
}