package com.example.shutthemouth

import retrofit2.Call
import retrofit2.http.*

interface STMAPI {
    @GET("/user/all")
    fun getUserAll(): Call<List<User>>

    @POST("/user/add")
    fun addUser(@Body user: User): Call<Int>

    @POST("/user/exist")
    fun isUserExist(@Query("id") userId: Int): Call<Boolean>

    @POST("/user/nameExist")
    fun isNameExist(@Query("name") name: String): Call<Boolean>

    @DELETE("/user/delete/{id}")
    fun deleteUser(@Path("id") userId: Int): Call<Void>

    @PUT("/user/avatar/{id}")
    fun setAvatar(@Path("id") userId: Int, @Body avatar: Int): Call<Void>

    @PUT("/user/ready")
    fun setReady(@Body user: User): Call<Void>

    @PUT("/user/banwords/{id}")
    fun setBanwords(@Path("id") userId: Int, @Body banwords: ArrayList<String>): Call<Void>

    @POST("/room/enter")
    fun enterRoom(@Body request: RoomEnterRequest): Call<Boolean>

    @PUT("/room/leave")
    fun leaveRoom(@Body request: RoomLeaveRequest): Call<Void>

    @PUT("/user/dead/{id}")
    fun setDead(@Path("id") userId: Int): Call<Void>

    @PUT("/room/add")
    fun addRoom(@Body room: Room): Call<Void>

    @GET("/room/all")
    fun getRoomList(): Call<List<Room>>

    data class RoomEnterRequest(val userId: Int, val roomId: Int)
    data class RoomLeaveRequest(val userId: Int, val currentRoom: Int)
}