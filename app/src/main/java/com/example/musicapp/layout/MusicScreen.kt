package com.example.musicapp.layout


import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentMusicScreenBinding
import com.example.musicapp.model.Music
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.Duration


class MusicScreen : BottomSheetDialogFragment() {

      lateinit var binding : FragmentMusicScreenBinding

      lateinit var updateSeekbar : Thread

      lateinit var handler: Handler



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicScreenBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data =  arguments?.get("music")
//        binding.mySeekbar.progress = (activity as MainActivity).mediaService.mediaPlayer!!.currentPosition
//        updateSeekbar = Thread(){
//            var totalDuration = (activity as MainActivity).mediaService.mediaPlayer!!.duration
//            var currentPosition = 0
//            while (currentPosition < totalDuration){
//                try {
//                    Thread.sleep(500)
//                    currentPosition = (activity as MainActivity).mediaService.mediaPlayer!!.currentPosition
//                    binding.mySeekbar.progress = currentPosition
//                }
//                catch (e : java.lang.NullPointerException){
//                    Log.d("error", e.toString())
//                }
//            }
//            if(currentPosition > totalDuration){
//
//                binding.playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
//                updateSeekbar.stop()
//            }
//
//        }
//        updateSeekbar.start()
//
//        handler = Handler()
//        handler.postDelayed(myRunnable,1000)
//
//        addData(data as Music)
//        addEvents()


    }







}