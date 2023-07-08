package com.example.shutthemouth.ui.readyRoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.shutthemouth.R
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.ActivityReadyBinding
import com.example.shutthemouth.ui.closet.ReadyAdapter

class ReadyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReadyBinding
    var ready_state = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReadyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Intent를 통해 받은 userList를 사용하여 UI 업데이트
        val userList = intent.getSerializableExtra("selectedUserList") as List<User>
        val adapter = ReadyAdapter(userList as ArrayList<User>, this)
        binding.readyGv.adapter = adapter

        //준비 완료!
        binding.readyBtn.setBackgroundResource(R.drawable.unready_btn)
        binding.readyBtn.setOnClickListener {
            ready_state = !ready_state
            if(ready_state) binding.readyBtn.setBackgroundResource(R.drawable.ready_btn) else binding.readyBtn.setBackgroundResource(R.drawable.unready_btn)
        }
    }
}