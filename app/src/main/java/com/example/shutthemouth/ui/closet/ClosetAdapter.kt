package com.example.shutthemouth.ui.closet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.shutthemouth.databinding.GvClosetItemBinding

class ClosetAdapter(items: ArrayList<ClosetItem>, context: Context) :
    BaseAdapter() {
    var gridItems: ArrayList<ClosetItem> = items
    var context: Context  = context
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return gridItems.size
    }

    override fun getItem(position: Int): Any {
        return gridItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: GvClosetItemBinding
        val view: View

        if (convertView == null) {
            binding = GvClosetItemBinding.inflate(inflater, parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as GvClosetItemBinding
            view = convertView
        }

        val gridItem = gridItems[position]
        // 데이터를 뷰에 바인딩합니다.
        binding.gvClosetItem.setImageResource(gridItem.img)


        return view
    }

}