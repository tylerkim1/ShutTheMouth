package com.example.shutthemouth.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.Room
import com.example.shutthemouth.STMAPI
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.FragmentMainBinding
import com.example.shutthemouth.ui.readyRoom.ReadyActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var roomGridView: GridView
    lateinit var roomList: List<Room>
    lateinit var refreshLayout : SwipeRefreshLayout

    lateinit var myData : User

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // setDummyData
        // setDummyMe()

        myData = PreferenceUtil(requireContext()).getUser("myUser")!!


        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        roomGridView = binding.mainGridview
        refreshLayout = binding.swipeRefreshLayout
        refreshLayout.setOnRefreshListener {
            refreshGridViewData()
        }
        roomList = ArrayList<Room>()

        getRoomList()

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
                    // var roomId = roomList.size + 1 // Or any other way to generate a unique room ID
                    val roomName = roomNameEditText.text.toString()
                    val roomMode = roomModeSpinner.selectedItem.toString()
                    val minPeople: Int = minPeopleNumberPicker.selectedItem.toString().toInt()
                    val maxPeople: Int = maxPeopleNumberPicker.selectedItem.toString().toInt()

                    // TODO: Call the API to create room


                    val mainUserList = arrayListOf<User>()
                    mainUserList.add(PreferenceUtil(requireContext()).getUser("myUser")!!)
                    val newRoom = Room("roomId", mainUserList, roomName, roomMode, minPeople, maxPeople, false)

                    val data = mapOf<String, Room>("room" to newRoom)
                    val call = ApiObject.getRetrofitService.addRoom(data)
                    call.enqueue(object: Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Toast.makeText(requireContext(), "Call Success", Toast.LENGTH_SHORT).show()
                            if(response.isSuccessful) {
                                val intent = Intent(requireContext(), ReadyActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }

        roomGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                requireContext(), roomList[position].roomId.toString() + " selected",
                Toast.LENGTH_SHORT
            ).show()


            val data = STMAPI.EnterRoomRequest(myData, roomList[position].roomId)
            val call = ApiObject.getRetrofitService.enterRoom(data)
            call.enqueue(object: Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Toast.makeText(requireContext(), "Call Success", Toast.LENGTH_SHORT).show()
                    if(response.isSuccessful) {
                        myData.currentRoom = roomList.get(position).roomId
                        PreferenceUtil(requireContext()).setUser("myUser",myData)
                        val intent = Intent(requireContext(), ReadyActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
                }
            })


        }

        return root
    }


    fun getRoomList() {

        val temp : List<Room>
        temp = ArrayList<Room>()


        val call = ApiObject.getRetrofitService.getRoomList()
        call.enqueue(object: Callback<List<Room>> {
            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                Toast.makeText(requireContext(), "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    roomList = response.body() ?: temp

                    val roomAdapter = MainGridViewAdapter(roomList, requireContext())
                    // on below line we are setting adapter to our grid view.
                    roomGridView.adapter = roomAdapter
                }
            }

            override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun refreshGridViewData() {
        // GridView 데이터를 새로고침하는 작업 수행
        // ...
        val temp : List<Room>
        temp = ArrayList<Room>()


        val call = ApiObject.getRetrofitService.getRoomList()
        call.enqueue(object: Callback<List<Room>> {
            override fun onResponse(call: Call<List<Room>>, response: Response<List<Room>>) {
                Toast.makeText(requireContext(), "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    roomList = response.body() ?: temp

                    val roomAdapter = MainGridViewAdapter(roomList, requireContext())
                    // on below line we are setting adapter to our grid view.
                    roomGridView.adapter = roomAdapter

                    // 새로고침 완료 시
                    refreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<List<Room>>, t: Throwable) {
                Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}