package com.example.musicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.adapter.MusicPlaylistAdapter
import com.example.musicapp.adapter.PlaylistMainAdapter
import com.example.musicapp.adapter.SearchAdapter
import com.example.musicapp.databinding.FragmentPlaylistListViewBinding
import com.example.musicapp.databinding.FragmentPlaylistSheetBinding
import com.example.musicapp.model.Music
import com.example.musicapp.model.MusicPlaylistData
import com.example.musicapp.model.Playlist
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.google.android.material.snackbar.Snackbar


class PlaylistListView : Fragment() {
    companion object {
        const val DATA = "key"
        const val TITLE = "title_key"
        fun newInstance(data: String,title: String): PlaylistListView {
            val fragment = PlaylistListView()
            val args = Bundle()
            args.putString(DATA, data)
            args.putString(TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
    lateinit var binding : FragmentPlaylistListViewBinding
    lateinit var adapter: MusicPlaylistAdapter
    private val token by lazy{
        "Bearer " + sharePreferencesUtils.getToken(requireContext())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistListViewBinding.inflate(layoutInflater)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MusicPlaylistAdapter(openMusic,deleteMusic)
        addEvents()
        binding.customToolbar.titleToolbar.text = arguments!!.get(TITLE).toString()
        binding.musicList.apply {
            adapter = this@PlaylistListView.adapter
        }
        (activity as MainActivity).viewModel.getPlaylistDataList(arguments!!.get(DATA).toString(),token)
        observerData()


    }

    private fun addEvents() {
        binding.customToolbar.backBtn.setOnClickListener {
            val fragmentManager = fragmentManager
            fragmentManager!!.popBackStack();
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()


    }
    private fun observerData() {
        (activity as MainActivity).viewModel.playlistData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { PlaylistDataResponse ->
                        adapter.differ.submitList(PlaylistDataResponse!!.data.array_music.toList())

                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
        (activity as MainActivity).viewModel.deleteMusicData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { DeletePlaylistResponse ->
                        (activity as MainActivity).viewModel.getPlaylistDataList(arguments!!.get(DATA).toString(),token)
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }

    private val openMusic : (Int,MutableList<MusicPlaylistData>) -> Unit = { pos, data ->
        val musicList : ArrayList<Music> = ArrayList()
        for(item in data){
            musicList.add(item.music)
        }
        (activity as MainActivity).startMusicFromService(pos,musicList)

    }

    private val deleteMusic : (String,String) -> Unit = { id, music_id ->
        (activity as MainActivity).viewModel.deleteMusic(id,music_id,token)

    }
}