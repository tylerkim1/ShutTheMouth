package com.example.shutthemouth.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shutthemouth.R
import com.example.shutthemouth.databinding.FragmentReadyBinding
import com.example.shutthemouth.ui.readyRoom.User


class ReadyFragment : Fragment() {

    private lateinit var binding : FragmentReadyBinding
    // This property is only valid between onCreateView and
    // onDestroyView.
    val userList = arrayListOf<User>()
    var ready_state = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(ReadyRoomViewModel::class.java)


        binding = FragmentReadyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val img = R.drawable.nubzuk_pant
        val img2 = R.drawable.nubzuki_glass
        val img3 = R.drawable.nubzuk_sunglass

        //유저 추가
        userList.add(User("안녕넙죽", "avatar2",  true, "hi", img))
        userList.add(User("반갑넙죽", "avatar2", true, "me", img2))
        userList.add(User("어쩔넙죽", "avatar2", true, "hello", img3))
        userList.add(User("저쩔넙죽", "avatar2", true, "good", img))
        userList.add(User("MZ넙죽", "avatar1", true, "shutup", img3))



        //어댑터 연결
        val adapter = ReadyAdapter(userList, requireContext())
        binding.readyGv.setAdapter(adapter)
        /*binding.closetGv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // MainActivity의 바인딩된 ImageView에 선택된 이미지를 설정합니다.
            binding.closetAvatar.setImageResource(closetItemList[position].avatar_img)
            Log.d(closetItemList[position].name, "item")
        }*/

        //준비 완료!
        binding.readyBtn.setBackgroundResource(R.drawable.unready_btn)
        binding.readyBtn.setOnClickListener {
            ready_state = !ready_state
            if(ready_state) binding.readyBtn.setBackgroundResource(R.drawable.ready_btn) else binding.readyBtn.setBackgroundResource(R.drawable.unready_btn)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    }



