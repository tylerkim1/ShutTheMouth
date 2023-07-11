package com.example.shutthemouth

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObject {
    private const val BASE_URL = "http://10.0.2.2:3000"

    private val getRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val getRetrofitService : STMAPI by lazy { getRetrofit.create(STMAPI::class.java) }
}