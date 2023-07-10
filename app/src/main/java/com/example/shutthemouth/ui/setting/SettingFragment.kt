package com.example.shutthemouth.ui.setting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shutthemouth.MainActivity
import com.example.shutthemouth.PreferenceUtil
import com.example.shutthemouth.R
import com.example.shutthemouth.databinding.FragmentSettingBinding
import com.example.shutthemouth.ui.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val avatarImageView = binding.settingAvatar
        val nameTextView = binding.settingName
        val logoutButton = binding.settingLogout

//        val userAvatar = PreferenceUtil(requireContext()).getInt("avatar", 0)
        val userName = PreferenceUtil(requireContext()).getString("name", "")

//        Log.d("SettingFragment", "User Avatar: $userAvatar")
//
//        avatarImageView.setImageResource(userAvatar)
        nameTextView.text = userName

        logoutButton.setOnClickListener {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(requireActivity()) {
                    signOutFun()
                }
        }

        return view
    }

    private fun signOutFun() {
        Toast.makeText(
            requireContext(), "Signed Out", Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}