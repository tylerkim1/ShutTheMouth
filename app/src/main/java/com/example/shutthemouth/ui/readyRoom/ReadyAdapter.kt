package com.example.shutthemouth.ui.closet

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.shutthemouth.R
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
        // binding.gvReadyItem.setImageResource(user.avatar)
        val resId = view.resources.getIdentifier(user.avatar, "drawable", "com.example.shutthemouth.ui.closet")
        binding.gvReadyItem.setImageResource(resId)
        if(user.isReady) {
            binding.readyroomReadySign.visibility = View.VISIBLE
        } else {
            binding.readyroomReadySign.visibility = View.INVISIBLE
        }
        //binding.gvReadyItemName.text = user.name

        return view
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        Log.d("updated", "now")
    }

}