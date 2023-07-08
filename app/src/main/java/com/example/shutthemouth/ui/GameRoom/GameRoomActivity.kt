package com.example.shutthemouth.ui.GameRoom

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.ResultActivity
import com.example.shutthemouth.Room
import com.example.shutthemouth.User
import com.example.shutthemouth.checkBanWord
import com.google.gson.Gson
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException
import java.net.Socket

class GameRoomActivity : AppCompatActivity() {

    private var userList = ArrayList<User>()
    private val chats = ArrayList<TestChat>()
    private lateinit var myData: User
    private var countDownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 0

    private lateinit var gridViewAdaptor : GridViewAdaptor
    private lateinit var dialog : Dialog

    private lateinit var currentRoom : Room
    val testArray = ArrayList<String>()

    lateinit var mSocket: io.socket.client.Socket

    private var recyclerView : RecyclerView? = null
    private lateinit var recyclerViewAdaptor : RecyclerViewAdaptor
    private var gridView : GridView? = null
    private var timerTextView : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // set dummy name
        PreferenceUtil(this).setString("name","younbae")
        PreferenceUtil(this).setString("avatar","avatar1")
        PreferenceUtil(this).setBoolean("isAlive",true)
        val testArray = ArrayList<String>()
        testArray.add("aa")
        getMe()

        mSocket = SocketApplication.get()
        mSocket.connect()
        mSocket.on("connection",onNewUser)
        mSocket.on("getMessage", onMessage)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_room)

        recyclerView = findViewById(R.id.gameroom_chat_board)
        recyclerView?.layoutManager = LinearLayoutManager(this@GameRoomActivity)
        recyclerViewAdaptor = RecyclerViewAdaptor(chats)
        recyclerView?.adapter = recyclerViewAdaptor

        setUserList()
        gridView = findViewById(R.id.gameroom_grid)
        gridViewAdaptor = GridViewAdaptor(this, userList)
        gridView?.adapter = gridViewAdaptor

        timerTextView = findViewById(R.id.gameroom_timer)

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.result_dialog)

        val exitButton = findViewById<Button>(R.id.gameroom_exit_button)
        exitButton.setOnClickListener {
            // 결과 창으로 이동
            goResultView()
        }

        val closeButton = dialog.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        startTimer()

        val sendButton = findViewById<Button>(R.id.gameroom_send)
        val chatEditText = findViewById<EditText>(R.id.gameroom_chat_textedit)
        sendButton.setOnClickListener {
            val tempText = chatEditText.text
            if(!tempText.isEmpty()) {
                // chats.add(TestChat(myData.name,tempText.toString(),myData.avatar, myData.currentRoom))
                sendChat(tempText.toString())
                recyclerViewAdaptor.notifyDataSetChanged()
                if(checkBanWord(tempText.toString(), myData)) {
                    Toast.makeText(this, "you died", Toast.LENGTH_SHORT).show()
                    PreferenceUtil(this).setBoolean("isAlive", false)
                    updateUser()
                    gridViewAdaptor.notifyDataSetChanged()
                    dialog.show()
                }
                resetTimer()
                chatEditText.setText("")
            }
        }
    }

    internal var onNewUser: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val length = args.size

            if (length == 0) {
                return@Runnable
            }
            //Here i'm getting weird error..................///////run :1 and run: 0
            var username = args[0].toString()
            try {
                val `object` = JSONObject(username)
                username = `object`.getString("username")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        })
    }

    var onMessage = Emitter.Listener { args ->
        Log.d("fdf","dfdfdfdfdfd")
        val obj = JSONObject(args[0].toString())
        Thread(object : Runnable{
            override fun run() {
                runOnUiThread(Runnable {
                    kotlin.run {
                        val msg = obj.get("chat").toString()
                        val name = obj.get("name").toString()
                        val avatar = obj.get("avatar") as Int
                        val room = obj.get("room") as Int
                        val tempChat = TestChat(name, msg, avatar, room)
                        chats.add(tempChat)
                        recyclerView!!.adapter?.notifyDataSetChanged()
                        recyclerView!!.scrollToPosition(recyclerViewAdaptor.itemCount-1)
                    }
                })
            }
        }).start()
    }

    private fun sendChat(message: String) {
        mSocket.emit("newMessage", Gson().toJson(TestChat(myData.name, message,myData.avatar,myData.currentRoom)))
        Log.d("log", Gson().toJson(TestChat(myData.name, message,myData.avatar,myData.currentRoom)))
    }

    private fun goResultView() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("avatar", myData.avatar)
        startActivity(intent)
    }

    private fun startTimer() {
        // 기존 타이머가 동작 중이면 중지
        stopTimer()

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                updateTimerText()
            }

            override fun onFinish() {
                timeRemaining = 0
                updateTimerText()
                Toast.makeText(this@GameRoomActivity, "you died", Toast.LENGTH_SHORT).show()
                PreferenceUtil(this@GameRoomActivity).setBoolean("isAlive", false)
                updateUser()
                gridViewAdaptor.notifyDataSetChanged()
                dialog.show()
            }
        }.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun resetTimer() {
        stopTimer()
        timeRemaining = 0
        updateTimerText()
        startTimer()
    }

    private fun updateTimerText() {
        val seconds = (timeRemaining / 1000).toInt()
        timerTextView!!.text = seconds.toString()
    }

    private fun updateUser() {
        // 나중에 서버에서 리스트 변화시 여기다가 받아올듯
        userList[1].isAlive = false
    }

    fun setUserList() {

        testArray.add("aa")
        userList.add(User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1))
        userList.add(User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1))
        userList.add(User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1))
        userList.add(User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1))
//        val call = ApiObject.getRetrofitService.getMyRoom(myData)
//        call.enqueue(object: Callback<Room> {
//            override fun onResponse(call: Call<Room>, response: Response<Room>) {
//                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
//                if(response.isSuccessful) {
//                    currentRoom = response.body() ?: Room(1,userList,"","",0,0,true)
//                    userList = currentRoom.users
//                }
//            }
//
//            override fun onFailure(call: Call<Room>, t: Throwable) {
//                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
//            }
//        })

    }
    fun getMe() {
        myData =  User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1)
//        val call = ApiObject.getRetrofitService.getMe(myData)
//        call.enqueue(object: Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                Toast.makeText(applicationContext, "Call Success", Toast.LENGTH_SHORT).show()
//                if(response.isSuccessful) {
//                    myData = response.body() ?: User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1)
//
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Toast.makeText(applicationContext, "Call Failed", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    inner class GridViewAdaptor(private var context: Context? ,private var userList: ArrayList<User>) : BaseAdapter() {
        override fun getCount(): Int {
            return userList.size
        }

        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var gridView = p1
            val holder: GridViewHolder
            if(gridView == null) {
                gridView = LayoutInflater.from(context).inflate(R.layout.chat_room_banword_item, null)
                holder = GridViewHolder()
                holder.banWord = gridView.findViewById(R.id.gameroom_banword)
                holder.name = gridView.findViewById(R.id.gameroom_name)
                holder.diedImageView = gridView.findViewById(R.id.gameroom_died_icon)
                gridView?.tag = holder
            } else {
                holder = gridView.tag as GridViewHolder
            }

            val itemData = userList[p0]
            if(itemData.name.equals(myData.name)) {
                holder.banWord.text = "??"
            } else {
                holder.banWord.text = itemData.banWord[0].toString()
            }

            holder.name.text = itemData.name.toString()
            if(!itemData.isAlive) {
                holder.diedImageView.visibility = View.VISIBLE
                holder.name.text = "사망"
            } else {
                holder.diedImageView.visibility = View.GONE
            }

            return gridView!!

        }

        inner class GridViewHolder() {
            lateinit var banWord: TextView
            lateinit var name: TextView
            lateinit var diedImageView: ImageView
        }

    }

    inner class RecyclerViewAdaptor(private var chats: ArrayList<TestChat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // get data
        private val VIEW_TYPE_RECEIVED = 0
        private val VIEW_TYPE_SENT = 1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when(viewType) {
                VIEW_TYPE_RECEIVED -> {
                    val view : View = LayoutInflater.from(parent.context).inflate(R.layout.gameroom_chat_item, parent, false)
                    MessageViewHolder(view)
                }
                VIEW_TYPE_SENT -> {
                    val view : View = LayoutInflater.from(parent.context).inflate(R.layout.gameroom_chat_me_item, parent, false)
                    MessageViewHolder2(view)
                }
                else -> throw IllegalArgumentException("invalid view")
            }
        }

        override fun getItemCount(): Int {
            return chats.size
        }

        override fun getItemViewType(position: Int): Int {
            val tempChat = chats[position]
            if (tempChat.name == PreferenceUtil(this@GameRoomActivity).getString("name", "")) {
                return VIEW_TYPE_SENT
            } else {
                return VIEW_TYPE_RECEIVED
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // if my chat?
            when(holder) {
                is MessageViewHolder -> {
                    holder.avatarImage.setImageResource(chats.get(position).avatar)
                    holder.chatText.text = chats.get(position).chat
                }
                is MessageViewHolder2 -> {
                    holder.avatarImage.setImageResource(chats.get(position).avatar)
                    holder.chatText.text = chats.get(position).chat
                }
                else -> throw IllegalArgumentException("view holder")
            }
        }


        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val avatarImage = view.findViewById<ImageView>(R.id.gameroom_you_avatar)
            val chatText = view.findViewById<TextView>(R.id.gameroom_you_chat)
        }

        inner class MessageViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
            val avatarImage = view.findViewById<ImageView>(R.id.gameroom_me_avatar)
            val chatText = view.findViewById<TextView>(R.id.gameroom_me_chat)
        }
    }


}