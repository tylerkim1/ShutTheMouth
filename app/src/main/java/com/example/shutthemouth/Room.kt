package com.example.shutthemouth

data class Room(
    val roomId: Int,
    val users : ArrayList<User>,
    val roomTitle: String,
    val roomMode: String,
    val roomMinPpl: Int,
    val roomMaxPpl: Int,
    val isStart: Boolean
    )