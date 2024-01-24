package com.example.musicapp.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.musicapp.MainActivity
import com.example.musicapp.adapter.MainAdapter
import com.example.musicapp.adapter.MusicAdapter
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


    lateinit var trendingAdapter : MusicAdapter

    lateinit var newMusicAdapter : MusicAdapter
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

        setUp();
        getData();
        observerData()
        addEvents()
    }

    private fun setUp() {
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        trendingAdapter = MusicAdapter(onClickMuic)
        newMusicAdapter = MusicAdapter(onClickMuic)
        binding.rvMusicHome.apply {
            this.adapter = trendingAdapter
        }
        binding.rvMusicHome2.apply {
            this.adapter = newMusicAdapter
        }
    }

    private fun getData() {
        viewModel.getTrending("trending")
        viewModel.getNew("new-music")
        binding.progessBar1.visibility = View.VISIBLE

    }

    private fun addEvents() {
      binding.playBtn.setOnClickListener {
          if(trendingAdapter.differ.currentList.size > 0){
              onClickMuic.invoke(0, trendingAdapter.differ.currentList)
          }
      }
    }

    private fun observerData(){
        viewModel.newMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        binding.progessBar2.visibility = View.GONE
                        newMusicAdapter.differ.submitList(MusicResponse.data)

                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    binding.progessBar2.visibility = View.VISIBLE
                }

            }
        }
        viewModel.trendingMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data?.let { MusicResponse ->
                        binding.progessBar1.visibility = View.GONE
                        trendingAdapter.differ.submitList(MusicResponse.data)
                        binding.songTitle.text = MusicResponse.data.get(0).name_music;
                        binding.artistTitle.text = MusicResponse.data.get(0).name_singer;
                        Glide.with(this).load(MusicResponse.data.get(0).image_music).into(binding.imageSong)

                    }
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    binding.progessBar1.visibility = View.VISIBLE
                }

            }
        }
    }
    private val onClickMuic : (Int,MutableList<Music>) -> Unit = { pos, data ->
       (activity as MainActivity).startMusicFromService(pos, data)

    }

}