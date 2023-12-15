package com.example.musicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.musicapp.application.MyApplication
import com.example.musicapp.component.MainComponent
import com.example.musicapp.databinding.ActivityLoginSrcreenBinding
import com.example.musicapp.databinding.ActivityWebviewScreenBinding
import com.example.musicapp.model.MusicDetailResponse
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.share.sharePreferencesUtils
import com.example.musicapp.utils.Resource
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModel
import com.example.musicapp.viewmodel.MusicViewModel.MusicViewModelFactory
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModel
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModelFactory
import javax.inject.Inject

class WebviewScreen : AppCompatActivity() {
    lateinit var binding : ActivityWebviewScreenBinding

    lateinit var component : MainComponent

    @Inject
    lateinit var repository : MusicRepository

    lateinit var viewModelFactory : MusicViewModelFactory
    lateinit var viewModel: MusicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        component = (application as MyApplication).component.getMainCompent().create(this)
        component.injectWebView(this)

        val id = intent.getStringExtra("music")
        viewModelFactory = MusicViewModelFactory(MyApplication(),repository)
        viewModel =  ViewModelProvider(this,viewModelFactory)[MusicViewModel::class.java]

        viewModel.getMusicDetail(id.toString())
        addEvents()
        observerData()
    }

    private fun observerData() {
        viewModel.musicDetail.observe(this, Observer {
            when(it){
                is Resource.Success -> {
                    it.data.let { MusicDetailResponse ->
                        val link = MusicDetailResponse!!.data.link_mv
                        binding.webView.webViewClient = object : WebViewClient(){
                            override fun onLoadResource(view: WebView?, url: String?) {
                                super.onLoadResource(view, url)
                            }
                        }
                        binding.webView.loadUrl("https://www.youtube.com/watch?v=$link")
                        binding.webView.webChromeClient = WebChromeClient()
                        val webViewSetting = binding.webView.settings
                        webViewSetting.javaScriptEnabled= true
                        webViewSetting.pluginState = WebSettings.PluginState.ON
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        })
    }

    private fun addEvents() {
        binding.toolbar.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}