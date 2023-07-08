package com.example.shutthemouth.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.shutthemouth.MainGridViewAdapter
import com.example.shutthemouth.R
import com.example.shutthemouth.Room
import com.example.shutthemouth.STMAPI
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.FragmentMainBinding
import com.example.shutthemouth.ui.GameRoom.GameRoomActivity
import com.example.shutthemouth.ui.readyRoom.ReadyActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var roomGridView: GridView
    lateinit var roomList: List<Room>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.mainAddRoom.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            // If you want to use custom layout in dialog
            val inflater = LayoutInflater.from(requireContext())
            val dialogLayout = inflater.inflate(R.layout.dialog_create_room, null)

            builder.setView(dialogLayout)
                .setTitle("Create Room")
                .setPositiveButton("Create") { dialog, _ ->
                    // TODO: Call the API to create room
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }

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
        userList.add(User(1,"abc","younbae", R.drawable.avatar2,true,true,testArray,1))
        userList.add(User(2,"ab","younba", R.drawable.avatar1,true,true,testArray,1))
        userList.add(User(3,"c","younb", R.drawable.avatar1,true,true,testArray,1))
        userList.add(User(4,"b","youn", R.drawable.avatar2,true,true,testArray,1))
        userList.add(User(5,"a","you", R.drawable.avatar1,true,true,testArray,1))

        val userList2 = arrayListOf<User>()
        userList2.add(User(6,"abc","younbae", R.drawable.avatar1,true,true,testArray,2))
        userList2.add(User(7,"ab","younba", R.drawable.avatar1,true,true,testArray,2))
        userList2.add(User(8,"c","younb", R.drawable.avatar1,true,true,testArray,2))
        userList2.add(User(9,"b","youn", R.drawable.avatar1,true,true,testArray,2))

        roomList = roomList + Room(1, userList,"입닥쳐 말포이", "기본모드", 3, 8, false)
        roomList = roomList + Room(2, userList2,"입닥쳐 해리포터", "기본모드", 6, 8, false)
        roomList = roomList + Room(3, userList,"입닥쳐 론", "기본모드", 1, 8, false)
        roomList = roomList + Room(4, userList,"입닥쳐 덤블도어", "기본모드", 7, 8, true)
        roomList = roomList + Room(5, userList,"입닥칠까말까 닥칠까말까 던던던던 던질까말까 던질까말까 던던전", "기본모드", 3, 8, false)

        val courseAdapter = MainGridViewAdapter(roomList, requireContext())

        // on below line we are setting adapter to our grid view.
        roomGridView.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
        roomGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                requireContext(), roomList[position].roomId.toString() + " selected",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(requireContext(), GameRoomActivity::class.java)
            startActivity(intent)
        }

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