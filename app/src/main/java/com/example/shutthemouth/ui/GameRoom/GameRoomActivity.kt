package com.example.shutthemouth.ui.GameRoom

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.ResultActivity
import org.w3c.dom.Text
import java.lang.IllegalArgumentException

class GameRoomActivity : AppCompatActivity() {

    private var userList = ArrayList<TestUser>()
    private val chats = ArrayList<TestChat>()
    private lateinit var myData: TestUser
    private var countDownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 0

    private lateinit var gridViewAdaptor : GridViewAdaptor
    private lateinit var dialog : Dialog

    private var recyclerView : RecyclerView? = null
    private var gridView : GridView? = null
    private var timerTextView : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // set dummy name
        PreferenceUtil(this).setString("name","반갑넙죽")
        PreferenceUtil(this).setString("avatar","avatar1")
        PreferenceUtil(this).setBoolean("isAlive",true)


        val testArray = ArrayList<String>()
        testArray.add("aa")
        myData = TestUser(PreferenceUtil(this).getString("name",""),PreferenceUtil(this).getString("avatar",""),PreferenceUtil(this).getBoolean("isAlive",true), testArray)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_room)

        setChat()
        recyclerView = findViewById(R.id.gameroom_chat_board)
        recyclerView?.layoutManager = LinearLayoutManager(this@GameRoomActivity)
        val recyclerViewAdaptor = RecyclerViewAdaptor(chats)
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
            if(!tempText.equals("")) {
                chats.add(TestChat(myData.name,tempText.toString(),myData.avtar, myData.isAlive))
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

    private fun goResultView() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("avatar", myData.avtar)
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

    private fun setChat() {
        chats.add(TestChat("반갑넙죽", "안녕하세요dxfgbxdgbxfgxfgnxfbfgbgnzgzdgbgbfgbbzdzdbnsssssasdjhvbidfbvksd\nbvnkladbvklhadbfvkladbfkladbfvkjadbnfkbadskfvbad;kjfvnkdafbvkadfbv", "avatar2",true))
        chats.add(TestChat("어쩔넙죽", "그런가요","avatar1",true))
        chats.add(TestChat("저쩔넙죽", "ㅋㅋ;","avatar1",true))
    }

    fun setUserList() {
        val testArray = ArrayList<String>()
        testArray.add("aa")
        userList.add(TestUser("안녕넙죽", "avatar2", true, testArray))
        userList.add(TestUser("반갑넙죽", "avatar2", true, testArray))
        userList.add(TestUser("어쩔넙죽", "avatar2", true, testArray))
        userList.add(TestUser("저쩔넙죽", "avatar2", true, testArray))
        userList.add(TestUser("MZ넙죽", "avatar1", true, testArray))
    }

    inner class GridViewAdaptor(private var context: Context? ,private var userList: ArrayList<TestUser>) : BaseAdapter() {


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
                    val resID = resources.getIdentifier("@drawable/"+chats.get(position).avatar, "drawable", "com.example.shutthemouth")
                    holder.avatarImage.setImageResource(resID)
                    holder.chatText.text = chats.get(position).chat
                }
                is MessageViewHolder2 -> {
                    val resID = resources.getIdentifier("@drawable/"+chats.get(position).avatar, "drawable", "com.example.shutthemouth")
                    holder.avatarImage.setImageResource(R.drawable.avatar2)
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