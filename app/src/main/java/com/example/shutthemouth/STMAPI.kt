package com.example.shutthemouth

import retrofit2.Call
import retrofit2.http.*

interface STMAPI {
    @GET("/user/all")
    fun getUserAll(): Call<List<User>>

    @GET("/user/get")
    fun getUser(@Body user: User): Call<User>

    @POST("/user/add")
    fun addUser(@Body user: Map<String, User>): Call<User>

    @POST("/user/getMe")
    fun getMe(@Body user: Map<String,User>): Call<User>

    @POST("/user/exist")
    fun isUserExist(@Body user: User): Call<Boolean>

    @POST("/user/nameExist")
    fun isNameExist(@Body user: Map<String, User>): Call<Boolean>

    @POST("/user/delete")
    fun deleteUser(@Body user: User): Call<Void>

    @POST("/user/avatar")
    fun setAvatar(@Body user: User): Call<Void>

    @POST("/user/ready")
    fun setReady(@Body user: User): Call<Void>

    @POST("/user/banwords")
    fun setBanwords(@Body user: Map<String, User>): Call<Void>

    @POST("/room/enter")
    fun enterRoom(@Body request: EnterRoomRequest): Call<Boolean>

    @POST("/room/leave")
    fun leaveRoom(@Body user: User): Call<Void>

    @POST("/user/dead")
    fun setDead(@Body user: User): Call<Void>

    @POST("/room/add")
    fun addRoom(@Body room: Map<String, Room>): Call<Void>

    @GET("/room/all")
    fun getRoomList(): Call<List<Room>>

    @POST("/room/getMyRoom")
    fun getMyRoom(@Body user: Map<String,User>): Call<Room>

    data class EnterRoomRequest(
        val user: User,
        val roomId: Int
    )
}