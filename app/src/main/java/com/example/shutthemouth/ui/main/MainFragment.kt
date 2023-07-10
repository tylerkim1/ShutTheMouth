package com.example.shutthemouth.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.Room
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.FragmentMainBinding
import com.example.shutthemouth.ui.readyRoom.ReadyActivity

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var roomGridView: GridView
    lateinit var roomList: List<Room>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        roomGridView = binding.mainGridview
        roomList = ArrayList<Room>()

//        CoroutineScope(Dispatchers.IO).launch {
//            val retrofit = Retrofit.Builder()
//                .baseUrl("http://Your-Base-Url.com/")  // use your base url
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            val service = retrofit.create(STMAPI::class.java)
//            val response = service.getRoomList().execute()
//
//            if (response.isSuccessful) {
//                roomList = response.body() ?: listOf()
//            }
//        }
        val userList = arrayListOf<User>()
        val testArray = ArrayList<String>()
        testArray.add("aa")
        //유저 추가
        userList.add(User("123124","abc","younbae", "avatar2",true,true,testArray,1))
        userList.add(User("123124","ab","younba", "avatar2",true,true,testArray,1))
        userList.add(User("123124","c","younb", "avatar2",true,true,testArray,1))
        userList.add(User("123124","b","youn", "avatar2",true,true,testArray,1))
        userList.add(User("123124","a","you", "avatar2",true,true,testArray,1))

        val userList2 = arrayListOf<User>()
        userList2.add(User("123124","abc","younbae", "avatar2",true,true,testArray,2))
        userList2.add(User("123124","ab","younba", "avatar2",true,true,testArray,2))
        userList2.add(User("123124","c","younb", "avatar2",true,true,testArray,2))
        userList2.add(User("123124","b","youn", "avatar2",true,true,testArray,2))

        roomList = roomList + Room(1, userList,"입닥쳐 말포이", "기본모드", 3, 8, false)
        roomList = roomList + Room(2, userList2,"입닥쳐 해리포터", "기본모드", 6, 8, false)
        roomList = roomList + Room(3, userList,"입닥쳐 론", "기본모드", 1, 8, false)
        roomList = roomList + Room(4, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(5, userList,"입닥칠까말까 닥칠까말까 던던던던 던질까말까 던질까말까 던던전", "기본모드", 3, 8, false)
        roomList = roomList + Room(6, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(7, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(8, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(9, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(10, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(11, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(12, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(13, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)

        binding.mainAddRoom.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            // If you want to use custom layout in dialog
            val inflater = LayoutInflater.from(requireContext())
            val dialogLayout = inflater.inflate(R.layout.dialog_create_room, null)

            // Get the EditText and Spinner from your dialog layout
            val roomNameEditText = dialogLayout.findViewById<EditText>(R.id.createRoom_editText)
            val roomModeSpinner = dialogLayout.findViewById<Spinner>(R.id.createRoom_mode_spinner)
            val minPeopleNumberPicker = dialogLayout.findViewById<Spinner>(R.id.createRoom_minPpl_spinner)
            val maxPeopleNumberPicker = dialogLayout.findViewById<Spinner>(R.id.createRoom_maxPpl_spinner)

            builder.setView(dialogLayout)
                .setTitle("Create Room")
                .setPositiveButton("Create") { dialog, _ ->
                    // Get user input
                    var roomId = roomList.size + 1 // Or any other way to generate a unique room ID
                    roomId = roomId.toInt()
                    val roomName = roomNameEditText.text.toString()
                    val roomMode = roomModeSpinner.selectedItem.toString()
                    val minPeople: Int = minPeopleNumberPicker.selectedItem.toString().toInt()
                    val maxPeople: Int = maxPeopleNumberPicker.selectedItem.toString().toInt()

                    // TODO: Call the API to create room
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val retrofit = Retrofit.Builder()
//                            .baseUrl("http://Your-Base-Url.com/")  // use your base url
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build()
//
//                        val service = retrofit.create(STMAPI::class.java)
//                        val response = service.createRoom(roomName, roomMode, minPeople, maxPeople).execute()
//
//                        if (response.isSuccessful) {
//                            // Add new room to the roomList and refresh the GridView
//                            withContext(Dispatchers.Main) {
//                                roomList = roomList + response.body()!!
//                                (roomGridView.adapter as MainGridViewAdapter).notifyDataSetChanged()
//                            }
//                        } else {
//                            // Show an error message if the API call failed
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(requireContext(), "Failed to create room", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }

                    // Create a dummy room

                    val userId = PreferenceUtil(requireContext()).getString("userId","")
                    val key = PreferenceUtil(requireContext()).getString("key","")
                    val name = PreferenceUtil(requireContext()).getString("name","")
                    val avatar = PreferenceUtil(requireContext()).getInt("avatar",R.drawable.avatar1)
                    val isReady = PreferenceUtil(requireContext()).getBoolean("isReady", false)
                    val isAlive = PreferenceUtil(requireContext()).getBoolean("isAlive",false)
                    val banWord = PreferenceUtil(requireContext()).getArrayListString("banWord")
                    val currentRoom = PreferenceUtil(requireContext()).getInt("currentRoom",-1)

                    Log.e("Avatar", "Avatar value: $avatar")
                    val mainUserList = arrayListOf<User>()
                    mainUserList.add(User(userId, key, name, "avatar2", isReady, isAlive, banWord, currentRoom))
                    val newRoom = Room(roomId, mainUserList, roomName, roomMode, minPeople, maxPeople, false)
                    roomList = roomList + newRoom

                    val intent = Intent(requireContext(), ReadyActivity::class.java).apply {
                        putExtra("selectedUserList", newRoom.users)  // Pass the new room's users
                    }
                    startActivity(intent)

                    // Refresh the GridView
//                    (roomGridView.adapter as MainGridViewAdapter).notifyDataSetChanged()

//                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }

        val roomAdapter = MainGridViewAdapter(roomList, requireContext())

        // on below line we are setting adapter to our grid view.
        roomGridView.adapter = roomAdapter

        // on below line we are adding on item
        // click listener for our grid view.
//        roomGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            Toast.makeText(
//                requireContext(), roomList[position].roomId.toString() + " selected",
//                Toast.LENGTH_SHORT
//            ).show()
//            val intent = Intent(requireContext(), GameRoomActivity::class.java)
//            startActivity(intent)
//        }

        roomGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                requireContext(), roomList[position].roomId.toString() + " selected",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(requireContext(), ReadyActivity::class.java).apply {
                putExtra("selectedUserList", roomList[position].users)
            }
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}