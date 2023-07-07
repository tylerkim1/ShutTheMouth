package com.example.shutthemouth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SetnameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setname)

        val button: Button = findViewById(R.id.setname_button)
        button.setOnClickListener {
            // MainActivity로 전환
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}