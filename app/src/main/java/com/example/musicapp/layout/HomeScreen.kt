package com.example.musicapp.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.adapter.MainAdapter
import com.example.musicapp.application.MyApplication
import com.example.musicapp.databinding.FragmentHomeScreenBinding
import com.example.musicapp.model.MainModel
import com.example.musicapp.model.Music
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModelFactory
import javax.inject.Inject


class HomeScreen : Fragment() {
    private lateinit var binding : FragmentHomeScreenBinding

    lateinit var viewModelFactory : MusicViewModelFactory

    lateinit var mainAdapter: MainAdapter
    lateinit var viewModel: MusicViewModel

    @Inject lateinit var repository : MusicRepository


    private val musicList = ArrayList<MainModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).component.injectHome(this)

        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        mainAdapter = MainAdapter(onClickMuic)
        binding.rvMusicHome.apply {
            this.adapter = mainAdapter
        }
        viewModel.getNew("new-music")
        observerData()
        addEvents()
    }

    private fun addEvents() {
      
    }

    private fun observerData(){
        viewModel.newMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        val newItem = MainModel("New Music",MusicResponse.data)
                        musicList.add(newItem)
                        mainAdapter.differ.submitList(musicList)
                        viewModel.getTrending("trending")
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                }

            }
        }
        viewModel.trendingMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        val newItem = MainModel("Trending",MusicResponse.data)
                        musicList.add(newItem)
                        mainAdapter.differ.submitList(musicList)
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    Toast.makeText(requireContext(),"Loading",Toast.LENGTH_LONG).show()
                }

            }
        }
    }
    private val onClickMuic : (Int,MutableList<Music>) -> Unit = { pos, data ->
       (activity as MainActivity).startMusicFromService(pos, data)

    }

}