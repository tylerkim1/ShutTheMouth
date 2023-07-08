package com.example.shutthemouth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

// on below line we are creating an
// adapter class for our grid view.
internal class MainGridViewAdapter(
    // on below line we are creating two
    // variables for course list and context
    private val courseList: List<Room>,
    private val context: Context
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null

    // below method is use to return the count of course list
    override fun getCount(): Int {
        return courseList.size
    }

    // below function is use to return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // below function is use to return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // in below function we are getting individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.room_card, null)
        }
        val room = courseList[position]

        val roomIdTextView: TextView = convertView!!.findViewById(R.id.main_roomId)
        roomIdTextView.text = room.roomId.toString()

        val roomTitleTextView: TextView = convertView.findViewById(R.id.main_roomTitle)
        roomTitleTextView.text = room.roomTitle

        val roomModeTextView: TextView = convertView.findViewById(R.id.main_roomMode)
        roomModeTextView.text = room.roomMode

        val roomCurPplTextView: TextView = convertView.findViewById(R.id.main_roomCurPpl)
        roomCurPplTextView.text = room.roomCurPpl.toString()

        val roomMaxPplTextView: TextView = convertView.findViewById(R.id.main_roomMaxPpl)
        roomMaxPplTextView.text = room.roomMaxPpl.toString()

        // at last we are returning our convert view.
        return convertView
    }
}
