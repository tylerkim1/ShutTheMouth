package com.example.shutthemouth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SetnameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setname)


        val button: Button = findViewById(R.id.setname_button)
        val nameInput: EditText = findViewById(R.id.setname_inputName)

        button.setOnClickListener {
            // EditText에 작성한 이름을 가져와 User 클래스의 인스턴스 생성
            val userName = nameInput.text.toString()
            val user = User(
                userId = 1,
                key = "temp",
                name = userName,
                avatar = 0,
                isReady = false,
                isAlive = false,
                banWord = ArrayList<String>(),
                currentRoom = 0
            )

            // MainActivity로 전환
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }
}