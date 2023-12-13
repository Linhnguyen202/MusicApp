package com.example.musicapp.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentProfileScreenBinding
import com.example.musicapp.share.sharePreferencesUtils


class ProfileScreen : Fragment() {
    lateinit var binding : FragmentProfileScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentProfileScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userName = sharePreferencesUtils.getUser(requireContext()).user_name
        val image = sharePreferencesUtils.getUser(requireContext()).image
        binding.nameTitle.text = userName
        Glide.with(requireContext()).load(image).into(binding.profileImage)
        binding.logoutBtn.setOnClickListener {
            sharePreferencesUtils.removeUser(requireContext())
            (activity as MainActivity).logout()
        }
    }

}