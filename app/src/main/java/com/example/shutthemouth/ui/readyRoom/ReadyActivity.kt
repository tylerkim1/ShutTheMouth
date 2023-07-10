package com.example.shutthemouth.ui.readyRoom

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.MainActivity
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.Room
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.ActivityReadyBinding
import com.example.shutthemouth.ui.GameRoom.GameRoomActivity
import com.example.shutthemouth.ui.GameRoom.SocketApplication
import com.example.shutthemouth.ui.closet.ReadyAdapter
import com.google.gson.Gson
import io.socket.emitter.Emitter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReadyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReadyBinding
    var ready_state = false

    lateinit var mSocket: io.socket.client.Socket
    lateinit var myData : User
    lateinit var currentRoom : Room
    lateinit var dialog : Dialog
    var userList = ArrayList<User>()
    lateinit var adapter : ReadyAdapter
    var readyCount = 0
    var submitCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReadyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val userId = PreferenceUtil(this).getInt("userId",1)
//        val name = PreferenceUtil(this).getString("name","")
//        val avatar = PreferenceUtil(this).getInt("avatar",1)
//        val receivedResult = 1

        // val receivedResult = intent.getStringExtra("result") as Int // 이전 화면에서 룸 정보 받아오기
        myData = User(1, null, "younbae4", "avatar2", false, true, ArrayList(),1)

        mSocket = SocketApplication.get()
        mSocket.connect()
        mSocket.emit("readyEnter", Gson().toJson(myData))
        mSocket.on("someoneReady", onReadyMessage)
        mSocket.on("someoneLeave", onLeaveMessage)
        mSocket.on("someoneEnter", onEnterMessage)
        mSocket.on("wordSubmit", onSubmitMessage)



        // Intent를 통해 받은 userList를 사용하여 UI 업데이트
        //val userList = intent.getSerializableExtra("selectedUserList") as ArrayList<User>? ?: arrayListOf()


        getMyRoom()

        //준비 완료!
        binding.readyBtn.setBackgroundResource(R.drawable.unready_btn)
        binding.readyBtn.setOnClickListener {
            if(myData.isReady) binding.readyBtn.setBackgroundResource(R.drawable.ready_btn) else binding.readyBtn.setBackgroundResource(R.drawable.unready_btn)
            myData.isReady = !myData.isReady
            val myIndex = userList.indexOfFirst { it.userId == myData.userId }
            userList.get(myIndex).isReady = myData.isReady
            mSocket.emit("ready", Gson().toJson(myData))
            adapter.notifyDataSetChanged()
            if(myData.isReady) {
                readyCount++
                checkAllReady()
            } else {
                readyCount--
            }
        }

        binding.readyTempbtn.setOnClickListener {
            val intent = Intent(this, GameRoomActivity::class.java)
            startActivity(intent)
        }

    }

    fun getMyRoom() {
        val data = mapOf<String,User>("user" to myData)
        val call = ApiObject.getRetrofitService.getMyRoom(data)
        call.enqueue(object: Callback<Room> {
            override fun onResponse(call: Call<Room>, response: Response<Room>) {
                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    currentRoom = response.body() ?: Room(1,userList,"","",0,0,true)
                    userList = currentRoom.users
                    for(i in userList) {
                        if(i.isReady) {
                            readyCount++
                            Log.d("ready++", i.avatar.toString())
                            Log.d("avatar", R.drawable.avatar2.toString())
                            Log.d("avatar", R.drawable.avatar1.toString())
                        }
                    }
                    Log.d("aa", "aa")
                    adapter = ReadyAdapter(userList, this@ReadyActivity)
                    binding.readyGv.adapter = adapter
                    // binding.readyGv.adapter = adapter
                }
            }

            override fun onFailure(call: Call<Room>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun checkAllReady() {
        if(userList.size == readyCount && readyCount>=currentRoom.roomMinPpl) {
            dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.readyroom_word_dialog)

            val nameText = dialog.findViewById<TextView>(R.id.ready_name_text)
            val banWordEdit = dialog.findViewById<EditText>(R.id.ready_word_edit)
            val banWordBtn = dialog.findViewById<Button>(R.id.ready_btn)

            val myIndex = userList.indexOfFirst { it.userId == myData.userId }

            var nextIndex = myIndex
            if(nextIndex + 1 == userList.size) {
                nextIndex = 0
            }

            nameText.text = "${userList.get(nextIndex).name}님의 금지어는.."

            banWordBtn.setOnClickListener {
                if (!banWordEdit.text.isEmpty()) {
                    banWordBtn.setBackgroundResource(R.drawable.ready_btn)
                    banWordBtn.isEnabled = false

                    val data = mapOf<String,User>("user" to userList.get(nextIndex))
                    val call = ApiObject.getRetrofitService.setBanwords(data)
                    call.enqueue(object: Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                            if(response.isSuccessful) {
                                mSocket.emit("submit", myData)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }

        }
    }

    var onReadyMessage = Emitter.Listener { args ->
        val obj = JSONObject(args[0].toString())
        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        val msg = obj.get("userId") as Int
                        if(msg != myData.userId) {
                            val myIndex = userList.indexOfFirst { it.userId == msg }
                            userList[myIndex].isReady = !userList[myIndex].isReady
                            if(userList[myIndex].isReady) {
                                readyCount++
                                checkAllReady()
                            } else {
                                readyCount--
                            }
                            adapter.notifyDataSetChanged()
                            checkAllReady()
                        }
                    }
                })
            }
        }).start()
    }

    var onSubmitMessage = Emitter.Listener { args ->
        val obj = JSONObject(args[0].toString())
        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        submitCount++
                        if(submitCount == userList.size) {
                            // todo
                            dialog.dismiss()

                            val intent = Intent(this@ReadyActivity, GameRoomActivity::class.java)
                            startActivity(intent)
                        }
                    }
                })
            }
        }).start()
    }

    var onLeaveMessage = Emitter.Listener { args ->
        Log.d("fdf","dfdfdfdfdfd")
        val obj = JSONObject(args[0].toString())
        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        val msg = obj.get("userId") as Int
                        val tempIsReady = obj.get("isReady") as Boolean
                        if(tempIsReady) {
                            readyCount--
                        }
                        val myIndex = userList.indexOfFirst { it.userId == msg }
                        userList.removeAt(myIndex)
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }).start()
    }

    var onEnterMessage = Emitter.Listener { args ->
        Log.d("fdf","dfdfdfdfdfd")
        val obj = JSONObject(args[0].toString())
        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        val userId = obj.get("userId") as Int
                        val userName = obj.get("name").toString()
                        val avatar = obj.get("avatar") as String
                        val tempUser = User(userId, null, userName, avatar, false, true, ArrayList(),currentRoom.roomId)
                        userList.add(tempUser)
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }).start()
    }
}