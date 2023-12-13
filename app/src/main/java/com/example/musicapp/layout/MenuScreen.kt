package com.example.musicapp.layout

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.adapter.PlaylistMainAdapter
import com.example.musicapp.adapter.PlaylistSheetAdapter
import com.example.musicapp.application.MyApplication
import com.example.musicapp.databinding.FragmentMenuScreenBinding
import com.example.musicapp.model.Playlist
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModel
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class MenuScreen : Fragment() {
    lateinit var binding : FragmentMenuScreenBinding
    lateinit var adapter: PlaylistMainAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMenuScreenBinding.inflate(layoutInflater)

        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).component.injectMenu(this)

        adapter = PlaylistMainAdapter(deletePlaylist,openEditPlaylistForm,openPlaylist)
        binding.playlistList.apply {
            adapter = this@MenuScreen.adapter
        }
        addEvents()
        getPlaylist()
        observerData()

    }

    private fun observerData() {
        (activity as MainActivity).viewModel.getPlaylist.observe(viewLifecycleOwner){
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
        (activity as MainActivity).viewModel.deletePlaylist.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { DeletePlalistResponse ->
                        Log.d("playlist",DeletePlalistResponse!!.data.toString())
                        Snackbar.make((activity as MainActivity).binding.mainContainer,DeletePlalistResponse!!.message,
                            Snackbar.LENGTH_SHORT).show()
                        getPlaylist()
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }

            }
        }
        
    }

    private fun addEvents() {
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addIcon -> {
                    addPlaylist()
                    true
                }
                R.id.searchIcon -> {
                    true
                }

                else -> {
                    true
                }
            }
        }
    }
    private fun getPlaylist() {
        val token = "Bearer " + sharePreferencesUtils.getToken(requireContext())
        (activity as MainActivity).viewModel.getUserPlaylist(token)

    }

    private fun addPlaylist() {
        PlaylistForm().show(parentFragmentManager,"mewtastTag")
    }


    private val deletePlaylist : (Playlist)->Unit = {
        val token = "Bearer " + sharePreferencesUtils.getToken(requireContext())
        (activity as MainActivity).viewModel.deleteUserPlaylist(it._id,token)
    }
    private val openEditPlaylistForm : (Playlist) -> Unit = {
        val playlistForm = PlaylistForm.newInstance(it._id)
        playlistForm.show(parentFragmentManager,"EDIT_FORM")
    }
    private val openPlaylist : (Playlist) -> Unit = {
        val fragment : PlaylistListView = PlaylistListView.newInstance(it._id,it.name_list)
        val fragmentManager = fragmentManager
        val fragmentTransaction : FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.addToBackStack("playlistView")
            .add(binding.playlistContainer.id,fragment,"playlistView").commit()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }




}