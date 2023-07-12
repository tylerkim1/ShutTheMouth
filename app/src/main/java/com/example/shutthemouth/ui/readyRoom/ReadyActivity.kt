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
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.Room
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.ActivityReadyBinding
import com.example.shutthemouth.ui.GameRoom.GameRoomActivity
import com.example.shutthemouth.ui.GameRoom.SocketApplication
import com.google.gson.Gson
import io.socket.emitter.Emitter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
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
        val tempList = ArrayList<String>()




        myData = PreferenceUtil(this).getUser("myUser")!!
        Log.d("myID", myData.userId!!)

        runBlocking {
//            adapter = ReadyAdapter(userList, this@ReadyActivity)
//            binding.readyGv.adapter = adapter
            getMyRoom()
        }


        // val receivedResult = intent.getStringExtra("result") as Int // 이전 화면에서 룸 정보 받아오기




        mSocket = SocketApplication.get()
        mSocket.connect()
//
        mSocket.emit("enter", Gson().toJson(myData))

        mSocket.emit("readyEnter", Gson().toJson(myData))
        mSocket.on("someoneReady", onReadyMessage)
        mSocket.on("someoneLeft", onLeaveMessage)
        mSocket.on("someoneEnter", onEnterMessage)
        mSocket.on("someoneSubmit", onSubmitMessage)

        // getMyRoom()

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

    }

    fun getMe() {
        val testArray = ArrayList<String>()

        // myData =  User(1,"abc1","younbae1", R.drawable.avatar2,true,true,testArray,1)
        val data = mapOf<String, User>("user" to myData)
        val call = ApiObject.getRetrofitService.getUser(data)
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    val tempData = response.body() ?: User("1","abc","younbae", "avatar2",true,true,testArray,"1")
                    myData.currentRoom = tempData.currentRoom
                    Log.d("my room ", myData.currentRoom)
                    PreferenceUtil(this@ReadyActivity).setUser("myUser",myData)
                    getMyRoom()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getMyRoom() {

//        userList.add(User("name", "asd","park","nupzuki",false,true, currentRoom = "1"))
//        adapter = ReadyAdapter(userList, this@ReadyActivity)
//        binding.readyGv.adapter = adapter
//

        val data = mapOf<String,User>("user" to myData)
        val call = ApiObject.getRetrofitService.getMyRoom(data)
        call.enqueue(object: Callback<Room> {
            override fun onResponse(call: Call<Room>, response: Response<Room>) {
                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    currentRoom = response.body() ?: Room("1",userList,"","",0,0,true)
                    userList = ArrayList(currentRoom.users)
                    for(i in userList) {
                        if(i.isReady) {
                            readyCount++
                        } else {
                            Log.d("not ready", i.name)
                        }
                    }
                    Log.d("aa", "aa")
                    adapter = ReadyAdapter(userList, this@ReadyActivity)
                    binding.readyGv.adapter = adapter

                    // adapter.notifyDataSetChanged()

                    // binding.readyGv.adapter.
                    Log.d("size",adapter.count.toString())
                }
            }

            override fun onFailure(call: Call<Room>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        val data = mapOf<String, User>("user" to myData)
        val call = ApiObject.getRetrofitService.leaveRoom(data)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                mSocket.emit("left", Gson().toJson(myData))
                myData.isReady = false
                PreferenceUtil(this@ReadyActivity).setUser("myUser",myData)
                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext, "leaved", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
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
            val banWordBtn = dialog.findViewById<Button>(R.id.ready_dialog_closeButton)

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

                    var nextPerson = userList.get(nextIndex)
                    val tempList = ArrayList<String>()
                    tempList.add(banWordEdit.text.toString())
                    nextPerson.banWord = tempList

                    val data = mapOf<String,User>("user" to nextPerson)
                    val call = ApiObject.getRetrofitService.setBanwords(data)
                    call.enqueue(object: Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
                            if(response.isSuccessful) {
                                mSocket.emit("submit", Gson().toJson(myData))
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }

            dialog.show()

        }
    }

    var onReadyMessage = Emitter.Listener { args ->
        Log.d("someone ready", "kkk")
        val obj: User = Gson().fromJson(args[0].toString(), User::class.java)
        Log.d("someone ready", obj.name)
        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        val userId = obj.userId
                        val myIndex = userList.indexOfFirst { it.userId == userId }
                        Log.d("someone ready", obj.name)
                        userList[myIndex].isReady = !userList[myIndex].isReady
                        if(userList[myIndex].isReady) {
                            readyCount++
                            checkAllReady()
                        } else {
                            readyCount--
                        }
                        adapter.updateData(userList)
                        adapter.notifyDataSetChanged()
                        checkAllReady()
                    }
                })
            }
        }).start()
    }

    var onSubmitMessage = Emitter.Listener { args ->
        val obj: User = Gson().fromJson(args[0].toString(), User::class.java)

        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {

                        submitCount++
                        Log.d("someone submit", obj.name)
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
        val obj: User = Gson().fromJson(args[0].toString(), User::class.java)

        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        val userId = obj.userId
                        val myIndex = userList.indexOfFirst { it.userId == userId }
                        val tempIsReady = obj.isReady as Boolean
                        if(tempIsReady) {
                            readyCount--
                        }
                        Log.d("someone leave", obj.name)
                        userList.removeAt(myIndex)
                        adapter.updateData(userList)
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }).start()
    }

    var onEnterMessage = Emitter.Listener { args ->
        val obj: User = Gson().fromJson(args[0].toString(), User::class.java)
        Log.d("enter!!","enter!!")

        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        if(obj.userId != myData.userId) {
                            userList.add(obj)
                            Log.d("someone Enter", obj.name)
                            adapter.updateData(userList)
                            adapter.notifyDataSetChanged()
                        } else {
                            Log.d("i am Enter", obj.name)
                        }
                    }
                })
            }
        }).start()
    }
}