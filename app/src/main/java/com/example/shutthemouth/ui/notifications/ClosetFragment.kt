package com.example.shutthemouth.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shutthemouth.R
import com.example.shutthemouth.databinding.FragmentClosetBinding

class ClosetFragment : Fragment() {

    private lateinit var binding : FragmentClosetBinding
    // This property is only valid between onCreateView and
    // onDestroyView.
    val avatarList = arrayListOf<Int>()
    var avIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ReadyRoomViewModel::class.java)


        binding = FragmentClosetBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.closetAvatar.setImageResource(R.drawable.nubzuki)

        //옷장 아이템 추가
        val closetItemList = arrayListOf<ClosetItem>()
        closetItemList.add(ClosetItem(R.drawable.item_sunglass, R.drawable.nubzuk_sunglass, "sunglass"))
        closetItemList.add(ClosetItem(R.drawable.item_pants, R.drawable.nubzuk_pant, "pant"))
        closetItemList.add(ClosetItem(R.drawable.item_glass, R.drawable.nubzuki_glass, "glass"))



        //어댑터 연결
        val adapter = ClosetAdapter(closetItemList, requireContext())
        binding.closetGv.setAdapter(adapter)
        binding.closetGv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // MainActivity의 바인딩된 ImageView에 선택된 이미지를 설정합니다.
            binding.closetAvatar.setImageResource(closetItemList[position].avatar_img)
            Log.d(closetItemList[position].name, "item")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun select_right() {
        if (avIdx != avatarList.size - 1) {
            binding.closetAvatar.setImageResource(avatarList[avIdx + 1])
            avIdx += 1
        }
    }

    fun select_left(){
        if(avIdx != 0){
            binding.closetAvatar.setImageResource(avatarList[avIdx-1])
            avIdx -=1
        }
    }



}