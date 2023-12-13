package com.example.musicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.adapter.PlaylistSheetAdapter
import com.example.musicapp.application.MyApplication
import com.example.musicapp.databinding.FragmentPlaylistSheetBinding
import com.example.musicapp.model.AddPlaylistBody
import com.example.musicapp.model.Playlist
import com.example.musicapp.model.UserPlaylistResponse
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModelFactory
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModel
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class PlaylistSheet : BottomSheetDialogFragment() {
    lateinit var binding : FragmentPlaylistSheetBinding
    lateinit var viewModelFactory : PlaylistViewModelFactory
    lateinit var viewModel: PlaylistViewModel
    @Inject
    lateinit var repository : MusicRepository
    lateinit var adapter: PlaylistSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).component.injectPlaylistSheet(this)
        viewModelFactory = PlaylistViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[PlaylistViewModel::class.java]
        adapter = PlaylistSheetAdapter(addMusicToPlaylist)
        binding.playlistList.apply {
            adapter = this@PlaylistSheet.adapter
        }
        addEvent()
        getPlaylist()
        observerData()

    }

    private fun observerData() {
        viewModel.getPlaylist.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { UserPlaylistResponse ->
                        Log.d("playlist",UserPlaylistResponse!!.data.toString())
                        adapter.differ.submitList(UserPlaylistResponse!!.data)
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }

            }
        }
        viewModel.addMusicToPlaylist.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { addPLaylistResponse ->
                        if(!addPLaylistResponse!!.data.toString().isNullOrEmpty()){
                            Snackbar.make(getDialog()?.getWindow()!!.getDecorView(),"Add song to playlist Successfully",Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }

            }
        }
    }

    private fun getPlaylist() {
        val token = "Bearer " + sharePreferencesUtils.getToken(requireContext())
        viewModel.getUserPlaylist(token)

    }

    private fun addEvent() {
        binding.addPlaylistBtn.setOnClickListener {
            PlaylistForm().show(parentFragmentManager,"ADD_FORM")
            this.dismiss()
        }
    }
    private val addMusicToPlaylist : (Playlist)-> Unit = {
        val token = "Bearer " + sharePreferencesUtils.getToken(requireContext())
        val musicId = (activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId
        val addPlaylistBody = AddPlaylistBody(it.name_list,it._id,musicId)
        viewModel.addMusicUserToPlaylist(addPlaylistBody, token)
    }

}