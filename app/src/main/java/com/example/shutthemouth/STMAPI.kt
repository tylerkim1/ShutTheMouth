package com.example.shutthemouth

import retrofit2.Call
import retrofit2.http.*

interface STMAPI {
    @GET("/user/all")
    fun getUserAll(): Call<List<User>>

    @GET("/user/get")
    fun getUser(@Body user: User): Call<User>

    @POST("/user/add")
    fun addUser(@Body user: Map<String,User>): Call<Int>

    @POST("/user/getMe")
    fun getMe(@Body user: User): Call<User>

    @POST("/user/exist")
    fun isUserExist(@Body user: User): Call<Boolean>

    @POST("/user/nameExist")
    fun isNameExist(@Body user: User): Call<Boolean>
//    @POST("/user/delete/{id}")
//    fun deleteUser(@Path("id") userId: Int): Call<Void>
    @POST("/user/delete")
    fun deleteUser(@Body user: User): Call<Void>
//    @POST("/user/avatar/{id}")
//    fun setAvatar(@Path("id") userId: Int, @Body avatar: Int): Call<Void>
    @POST("/user/avatar")
    fun setAvatar(@Body user: User): Call<Void>

    @POST("/user/ready")
    fun setReady(@Body user: User): Call<Void>

    @POST("/user/banwords")
    fun setBanwords(@Body user: User): Call<Void>

    @POST("/room/enter")
    fun enterRoom(@Body user: User): Call<Boolean>

    @POST("/room/leave")
    fun leaveRoom(@Body user: User): Call<Void>

    @POST("/user/dead")
    fun setDead(@Body user: User): Call<Void>

    @POST("/room/add")
    fun addRoom(@Body room: Room): Call<Void>

    @GET("/room/all")
    fun getRoomList(): Call<List<Room>>

    @GET("/room/getMyRoom")
    fun getMyRoom(@Body user: User): Call<Room>

//    data class RoomEnterRequest(val userId: Int, val roomId: Int)
//    data class RoomLeaveRequest(val userId: Int, val currentRoom: Int)
}