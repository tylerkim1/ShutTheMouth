package com.example.shutthemouth.ui.temp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.FragmentTempBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TempFragment : Fragment() {

    private var userList = listOf<User>()
    private var _binding: FragmentTempBinding? = null
    lateinit var listAdapter: ListAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTempBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listAdapter = ListAdapter()

        binding.btn01.setOnClickListener {
            listAdapter.setList(userList)
            listAdapter.notifyDataSetChanged()
        }

        binding.recycler01.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        initList()

        return root
    }

    private fun initList() {
        val call = ApiObject.getRetrofitService.getUserAll()
        call.enqueue(object: Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                Toast.makeText(requireContext(), "Call Success", Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    userList = response.body() ?: listOf()
                    listAdapter.setList(userList)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}