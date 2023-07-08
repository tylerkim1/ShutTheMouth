package com.example.shutthemouth

data class Room(
    var roomId: Int,
    var users : ArrayList<User>,
    var roomTitle: String,
    var roomMode: String,
    var roomMinPpl: Int,
    var roomMaxPpl: Int,
    var isStart: Boolean
    )
