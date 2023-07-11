package com.example.shutthemouth.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.MainActivity
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                userId = "final trial",
                key = "temp",
                name = userName,
                avatar = "avatar1",
                isReady = false,
                isAlive = false,
                banWord = ArrayList<String>(),
                currentRoom = "0"
            )

            var data = mapOf("user" to user)
            val call = ApiObject.getRetrofitService.isNameExist(data)
            call.enqueue(object: Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful && response.body() == true) {
                        Toast.makeText(this@SetnameActivity, "이름이 이미 존재합니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Name does not exist, add the user
                        val addUserCall = ApiObject.getRetrofitService.addUser(data)
                        addUserCall.enqueue(object: Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    // User added successfully, proceed with saving the user and moving to next activity
                                    PreferenceUtil(this@SetnameActivity).setString("userId",
                                        response.body()?.userId!!)
                                    PreferenceUtil(this@SetnameActivity).setString("key","temp")
                                    PreferenceUtil(this@SetnameActivity).setString("name",userName)
                                    PreferenceUtil(this@SetnameActivity).setInt("avatar", R.drawable.nubzuki)

                                    // MainActivity로 전환
                                    Log.d("result", response.body().toString())
                                    val intent = Intent(this@SetnameActivity, MainActivity::class.java)
                                    intent.putExtra("user", user)
                                    startActivity(intent)
                                } else {
                                    Log.d("HTTP error code", response.code().toString())
                                    Toast.makeText(this@SetnameActivity, "서버 에러: ${response.code()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Toast.makeText(this@SetnameActivity, "유저 추가 네트워크 오류.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(this@SetnameActivity, "네트워크 오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}