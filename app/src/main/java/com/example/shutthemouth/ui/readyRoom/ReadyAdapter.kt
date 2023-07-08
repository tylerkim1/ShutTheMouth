package com.example.shutthemouth.ui.closet


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.shutthemouth.databinding.GvReadyItemBinding
import com.example.shutthemouth.User


class ReadyAdapter(items: ArrayList<User>, context: Context) :
    BaseAdapter() {
    var userList: ArrayList<User> = items
    var context: Context  = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: GvReadyItemBinding
        val view: View

        if (convertView == null) {
            binding = GvReadyItemBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as GvReadyItemBinding
            view = convertView
        }

        val user = userList[position]
        // 데이터를 뷰에 바인딩합니다.
        binding.gvReadyItem.setImageResource(user.img)
        //binding.gvReadyItemName.text = user.name


        return view
    }

}