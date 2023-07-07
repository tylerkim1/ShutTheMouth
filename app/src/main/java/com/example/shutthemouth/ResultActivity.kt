package com.example.shutthemouth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.shutthemouth.ui.GameRoom.TestUser

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val rankString = getRank()

        val rankTextView = findViewById<TextView>(R.id.resultview_rank)
        rankTextView.text = rankString

        val avatarImageView = findViewById<ImageView>(R.id.resultview_myavatar)
        val receivedData = intent.getStringExtra("avatar")
        val resID = resources.getIdentifier("@drawable/"+receivedData, "drawable", "com.example.shutthemouth")

        avatarImageView.setImageResource(resID)
    }

    fun getRank() : String {
        val testArray = ArrayList<String>()
        val userList = ArrayList<TestUser>()
        testArray.add("aa")
        userList.add(TestUser("안녕넙죽", "avatar2", true, testArray))
        userList.add(TestUser("반갑넙죽", "avatar2", true, testArray))
        userList.add(TestUser("어쩔넙죽", "avatar2", true, testArray))
        userList.add(TestUser("저쩔넙죽", "avatar2", true, testArray))
        userList.add(TestUser("MZ넙죽", "avatar1", true, testArray))
        var rankString : String = ""
        var ranking : Int = 1
        for(i in userList) {
            rankString = rankString +"#" +ranking.toString() + " " + i.name + "\n"
            ranking ++
        }

        return rankString
    }
}