package com.example.shutthemouth

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shutthemouth.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var myData: User = User(
        userId = "",
        key = "999",
        name = "-1",
        avatar = "",
        isReady = false,
        isAlive = false,
        banWord = ArrayList<String>(),
        currentRoom = "0"
    )
    private lateinit var player: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("userId", myData.toString())
        val userId = PreferenceUtil(this).getString("userId", "")
        Log.d("userId", userId.toString())
        myData.userId = userId
        Log.d("changeduserId", myData.toString())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        player = MediaPlayer.create(this, R.raw.music);
        player.start();

        Log.d("this user", myData.toString())
        val data = mapOf<String,User>("user" to myData)
        val call = ApiObject.getRetrofitService.getUser(data)
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    val myUser = response.body()
                    Log.d("this user", myUser.toString())
                    if (myUser != null) {
                        Toast.makeText(applicationContext, myUser.name + " 님 환영합니다!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }



}