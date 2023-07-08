package com.example.shutthemouth

import com.example.shutthemouth.ui.GameRoom.TestUser
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

fun checkBanWord(word: String, user: User) : Boolean {
    for(i in user.banWord) {
        if (word.contains(i)) {
            return true
        }
    }
    return false
}
