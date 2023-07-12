package com.example.shutthemouth

import java.io.Serializable

data class User (
    var userId : String? = "-999",
    var key : String? = "-999",
    var name : String = "-999",
    var avatar : String = "nupzuki",
    var isReady : Boolean = false,
    var isAlive : Boolean = true,
    var banWord : ArrayList<String> = ArrayList(),
    var currentRoom : String = "-999",
): Serializable

fun checkBanWord(word: String, user: User) : Boolean {
    for(i in user.banWord) {
        if (word.contains(i)) {
            return true
        }
    }
    return false
}
