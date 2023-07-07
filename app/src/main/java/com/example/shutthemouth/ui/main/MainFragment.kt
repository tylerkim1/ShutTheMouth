package com.example.shutthemouth.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shutthemouth.MainGridViewAdapter
import com.example.shutthemouth.MainRoom
import com.example.shutthemouth.R
import com.example.shutthemouth.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    lateinit var roomGridView: GridView
    lateinit var roomList: List<MainRoom>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root

        roomGridView = binding.mainGridview
        roomList = ArrayList<MainRoom>()

        roomList = roomList + MainRoom(1, "입닥쳐 말포이", "기본모드", 3, 8)
        roomList = roomList + MainRoom(2, "입닥쳐 해리포터", "기본모드", 6, 8)
        roomList = roomList + MainRoom(3, "입닥쳐 론", "기본모드", 1, 8)
        roomList = roomList + MainRoom(4, "입닥쳐 덤블도어", "기본모드", 7, 8)
        roomList = roomList + MainRoom(5, "입닥칠까말까 닥칠까말까 던던던던 던질까말까 던질까말까 던던전", "기본모드", 7, 8)

        val courseAdapter = MainGridViewAdapter(courseList = roomList, context = requireContext())

        // on below line we are setting adapter to our grid view.
        roomGridView.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
        roomGridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                requireContext(), roomList[position].roomId.toString() + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}