package com.example.shutthemouth

import com.example.shutthemouth.ui.GameRoom.TestUser
import java.io.Serializable

data class User (
    var userId : Int = -1,
    var key : String? = null,
    var name : String = "",
    var avatar : Int = 0,
    var isReady : Boolean = false,
    var isAlive : Boolean = false,
    var banWord : ArrayList<String> = ArrayList(),
    var currentRoom : Int = -1,
): Serializable

fun checkBanWord(word: String, user: User) : Boolean {
    for(i in user.banWord) {
        if (word.contains(i)) {
            return true
        }
    }
    return false
}
