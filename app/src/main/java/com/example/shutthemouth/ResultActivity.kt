package com.example.shutthemouth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.shutthemouth.ui.GameRoom.TestUser

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val receivedResult = intent.getStringExtra("result")
        val rankTextView = findViewById<TextView>(R.id.resultview_rank)
        rankTextView.text = receivedResult

        val avatarImageView = findViewById<ImageView>(R.id.resultview_myavatar)
        val receivedData = intent.getStringExtra("avatar")
        val resID = resources.getIdentifier("@drawable/"+receivedData, "drawable", "com.example.shutthemouth")
        avatarImageView.setImageResource(resID)

        val exitButton = findViewById<Button>(R.id.resultview_exit)
        exitButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}