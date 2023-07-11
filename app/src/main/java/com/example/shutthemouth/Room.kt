package com.example.shutthemouth

import java.io.Serializable

data class Room(
    var roomId: String,
    var users : ArrayList<User>,
    var roomTitle: String,
    var roomMode: String,
    var roomMinPpl: Int,
    var roomMaxPpl: Int,
    var isStart: Boolean
    ) : Serializable
