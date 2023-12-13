package com.example.musicapp.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.MainActivity
import com.example.musicapp.adapter.SearchAdapter
import com.example.musicapp.application.MyApplication
import com.example.musicapp.databinding.FragmentSearchScreenBinding
import com.example.musicapp.model.Music
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SearchScreen : Fragment() {
    lateinit var binding: FragmentSearchScreenBinding
    var searchText = ""
    @Inject
    lateinit var repository : MusicRepository

    lateinit var viewModel: MusicViewModel
    lateinit var viewModelFactory : MusicViewModelFactory

    lateinit var searchAdapter: SearchAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).component.injectSearch(this)
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]
        searchAdapter = SearchAdapter(onClickMuic)
        binding.searchList.apply {
            adapter = searchAdapter
        }
        addEvents()
        viewModel.searchMusic.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    it.data.let { MusicResponse ->
                        searchAdapter.differ.submitList(MusicResponse!!.data.toList())
                        if(MusicResponse!!.data.size == 0){
                            binding.searchTextFail.visibility = View.VISIBLE
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

    private fun addEvents() {
        var job : Job? = null
        binding.searchEditText.addTextChangedListener{
            binding.searchTextFail.visibility = View.GONE
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        viewModel.getSearching(it.toString())
                    }

                }
            }
        }
    }

    private val onClickMuic : (Int,MutableList<Music>) -> Unit = { pos, data ->
        (activity as MainActivity).startMusicFromService(pos, data)

    }

}