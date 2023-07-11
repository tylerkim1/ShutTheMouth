package com.example.shutthemouth

import java.io.Serializable

data class User (
    var userId : String? = null,
    var key : String? = null,
    var name : String = "",
    var avatar : String = "avatar1",
    var isReady : Boolean = false,
    var isAlive : Boolean = false,
    var banWord : ArrayList<String> = ArrayList(),
    var currentRoom : String = "",
): Serializable

fun checkBanWord(word: String, user: User) : Boolean {
    for(i in user.banWord) {
        if (word.contains(i)) {
            return true
        }
    }
    return false
}
