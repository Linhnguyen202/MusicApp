package com.example.musicapp.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentMusicScreenBinding
import com.example.musicapp.databinding.FragmentMusicSettingSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MusicSettingSheet : BottomSheetDialogFragment() {
   lateinit var binding : FragmentMusicSettingSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicSettingSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEvent()
    }

    private fun addEvent() {
        binding.addPlaylistBtn.setOnClickListener {
            PlaylistSheet().show(parentFragmentManager,"playListSheet")
            this.dismiss()
        }
        binding.watchMvBtn.setOnClickListener {
            (activity as MainActivity).getMv((activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId)
        }
    }

}