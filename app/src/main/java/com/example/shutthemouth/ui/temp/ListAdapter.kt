package com.example.shutthemouth.ui.temp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.ItemListBinding

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyView>() {
    private var userList = listOf<User>()

    inner class MyView(private val binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pos: Int) {
            binding.text01.text = userList[pos].name
            binding.text02.text = userList[pos].name
            binding.text03.text = userList[pos].name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val view = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyView(view)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setList(list: List<User>) {
        userList = list
    }
}