package com.example.shutthemouth.ui.GameRoom

data class TestUser (
    val name: String,
    val avtar: String,
    var isAlive: Boolean,
    val banWord: ArrayList<String>
    )

fun checkBanWord(word: String, user: TestUser) : Boolean {
    for(i in user.banWord) {
        if (word.contains(i)) {
            return true
        }
    }
    return false
}