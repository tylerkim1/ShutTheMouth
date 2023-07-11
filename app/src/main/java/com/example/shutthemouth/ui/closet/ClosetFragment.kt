package com.example.shutthemouth.ui.closet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shutthemouth.ApiObject
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.Room
import com.example.shutthemouth.User
import com.example.shutthemouth.databinding.FragmentClosetBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        binding = FragmentClosetBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentNubzuk = PreferenceUtil(requireContext()).getString("avatar", "")
        val resId = resources.getIdentifier(currentNubzuk, "drawable", "com.example.shutthemouth.ui.closet")
        binding.closetAvatar.setImageResource(resId)
        if(currentNubzuk == "nubzuki") {
            binding.closetBack.setImageResource(R.drawable.closet_back_kaist)
        } else if(currentNubzuk == "nubzuki_sunglass") {
            binding.closetBack.setImageResource(R.drawable.closet_back_ufo)
        } else if(currentNubzuk == "nubzuki_pant") {
            binding.closetBack.setImageResource(R.drawable.closet_back_mun)
        } else if(currentNubzuk == "nubzuki_glass") {
            binding.closetBack.setImageResource(R.drawable.closet_back_club)
        } else if(currentNubzuk == "nubzuk_mask") {
            binding.closetBack.setImageResource(R.drawable.closet_back_kaist)
        } else {
            binding.closetBack.setImageResource(R.drawable.closet_back_kaist)
        }


        //옷장 아이템 추가
        val closetItemList = arrayListOf<ClosetItem>()
        closetItemList.add(ClosetItem(R.drawable.gameroom_died, R.drawable.nubzuki, "nubzuki",R.drawable.closet_back_kaist))
        closetItemList.add(ClosetItem(R.drawable.item_sunglass, R.drawable.nubzuk_sunglass, "nubzuki_sunglass",R.drawable.closet_back_ufo))
        closetItemList.add(ClosetItem(R.drawable.item_pants, R.drawable.nubzuk_pant, "nubzuki_pant",R.drawable.closet_back_mun))
        closetItemList.add(ClosetItem(R.drawable.item_glass, R.drawable.nubzuki_glass, "nubzuki_glass",R.drawable.closet_back_club))
        closetItemList.add(ClosetItem(R.drawable.nubzuk_mask_item, R.drawable.nubzuk_mask, "nubzuk_mask",R.drawable.closet_back_club))

        //어댑터 연결
        val adapter = ClosetAdapter(closetItemList, requireContext())
        binding.closetGv.setAdapter(adapter)
        binding.closetGv.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            // MainActivity의 바인딩된 ImageView에 선택된 이미지를 설정합니다.
            binding.closetAvatar.setImageResource(closetItemList[position].avatar_img)
            binding.closetBack.setImageResource(closetItemList[position].back)
            PreferenceUtil(requireContext()).setString("avatar", closetItemList[position].name)
            setAvatar()
            Log.d(closetItemList[position].name, "item")
        }

        return root
    }

    fun setAvatar() {
        val tempUser = User()
        tempUser.userId = PreferenceUtil(requireContext()).getString("userId","")
        tempUser.avatar = PreferenceUtil(requireContext()).getString("avatar","")
        val data = mapOf<String, User>("user" to tempUser)
        val call = ApiObject.getRetrofitService.setAvatar(data)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful) {
                    Toast.makeText(requireContext(),"아바타 변경 완료!",Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Call Failed", Toast.LENGTH_SHORT).show()
            }
        })
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