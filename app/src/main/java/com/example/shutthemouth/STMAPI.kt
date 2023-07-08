package com.example.shutthemouth

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface STMAPI {
    @GET("/user/all")
    fun getUserAll(): Call<List<User>>
    @POST("/user/addUser")
}