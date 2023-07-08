package com.example.shutthemouth

import java.io.Serializable

data class User (
    var userId : Int,
    var key : String,
    var name : String,
    var avatar : Int,
    var isReady : Boolean,
    var isAlive : Boolean,
    var banWord : ArrayList<String>,
    var currentRoom : Int,
): Serializable