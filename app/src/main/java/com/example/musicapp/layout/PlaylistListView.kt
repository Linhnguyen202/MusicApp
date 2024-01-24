package com.example.musicapp.layout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.adapter.MusicPlaylistAdapter
import com.example.musicapp.adapter.PlaylistMainAdapter
import com.example.musicapp.adapter.SearchAdapter
import com.example.musicapp.application.MyApplication
import com.example.musicapp.databinding.FragmentPlaylistListViewBinding
import com.example.musicapp.databinding.FragmentPlaylistSheetBinding
import com.example.musicapp.model.Music
import com.example.musicapp.model.MusicPlaylistData
import com.example.musicapp.model.Playlist
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModel
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class PlaylistListView : Fragment() {

    lateinit var viewModelFactory : PlaylistViewModelFactory
    lateinit var viewModel: PlaylistViewModel
    @Inject
    lateinit var repository : MusicRepository
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
        (activity as MainActivity).component.injectPlaylistMusic(this)
        setup()
        viewModel.getPlaylistDataList(requireArguments().get(DATA).toString(),token)
        observerData()


    }

    private fun setup() {
        viewModelFactory = PlaylistViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[PlaylistViewModel::class.java]
        adapter = MusicPlaylistAdapter(openMusic,deleteMusic)
        addEvents()
        binding.customToolbar.titleToolbar.text = requireArguments().get(TITLE).toString()
        binding.musicList.apply {
            adapter = this@PlaylistListView.adapter
        }
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
        viewModel.playlistData.observe(viewLifecycleOwner){
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
        viewModel.deleteMusicData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { DeletePlaylistResponse ->
                        (activity as MenuScreen).viewModel.getPlaylistDataList(requireArguments().get(DATA).toString(),token)
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
        (requireParentFragment() as MenuScreen).viewModel.deleteMusic(id,music_id,token)

    }
}