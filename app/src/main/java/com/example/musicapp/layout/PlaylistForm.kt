package com.example.musicapp.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.application.MyApplication
import com.example.musicapp.databinding.FragmentNewPlaylistFormBinding
import com.example.musicapp.model.EditPlaylistBody
import com.example.musicapp.model.PlaylistBody
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModel
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class PlaylistForm : BottomSheetDialogFragment() {
    companion object {
        const val ARG_DATA = "key"
        fun newInstance(data: String,): PlaylistForm {
            val fragment = PlaylistForm()
            val args = Bundle()
            args.putString(ARG_DATA, data)
            fragment.arguments = args
            return fragment
        }
    }

   private val token by lazy{
       "Bearer " + sharePreferencesUtils.getToken(requireContext())
   }

    lateinit var binding : FragmentNewPlaylistFormBinding
    lateinit var viewModelFactory : PlaylistViewModelFactory
    lateinit var viewModel: PlaylistViewModel
    @Inject
    lateinit var repository : MusicRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewPlaylistFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).component.injectPlaylistForm(this)
        viewModelFactory = PlaylistViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[PlaylistViewModel::class.java]
        Toast.makeText(requireContext(),arguments?.getString("key"),Toast.LENGTH_LONG).show()
        addEvent()
        observerData()

    }

    private fun observerData() {
        (activity as MainActivity).viewModel.addPlaylist.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { PlaylistResponse ->
                        if(!PlaylistResponse!!.toString().isNullOrEmpty()){
                            Snackbar.make(getDialog()?.getWindow()!!.getDecorView(),"Add playlist Successfully",
                                Snackbar.LENGTH_SHORT).show()
                            binding.titleEditText.text!!.clear()
                            (activity as MainActivity).viewModel.getUserPlaylist(token)
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }

            }
        }
        (activity as MainActivity).viewModel.updateNamePlaylist.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { UpdatePlaylistResponse ->
                        if(!UpdatePlaylistResponse!!.toString().isNullOrEmpty()){
                            Snackbar.make(getDialog()?.getWindow()!!.getDecorView(),"Update playlist Successfully",
                                Snackbar.LENGTH_SHORT).show()
                            binding.titleEditText.text!!.clear()
                            (activity as MainActivity).viewModel.getUserPlaylist(token)
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

    private fun addEvent() {
        binding.cancelBtn.setOnClickListener {
            this.dismiss()
        }
        binding.submitBtn.setOnClickListener {
            when(tag){
                "ADD_FORM" -> {
                    addPlaylist()
                }
                "EDIT_FORM" -> {
                    editPlaylist()
                }
            }

        }
    }

    private fun editPlaylist() {
        val playList = binding.titleEditText.text.toString()
        val editPlaylistBody = EditPlaylistBody(playList,arguments?.getString("key").toString())
        (activity as MainActivity).viewModel.updateNamePlaylist(editPlaylistBody,token)

    }

    private fun addPlaylist() {
        val playList = binding.titleEditText.text.toString()
        val musicId = (activity as MainActivity).mediaService.player!!.currentMediaItem!!.mediaId
        val playlistBody = PlaylistBody(musicId,playList)
        (activity as MainActivity).viewModel.addUserPlaylist(playlistBody,token)
    }

}