package com.example.musicapp

import android.annotation.SuppressLint
import android.content.*
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder

import android.view.MenuItem
import android.view.MotionEvent

import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.constraintlayout.widget.ConstraintSet.Motion

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.musicapp.adapter.ViewPagerAdapter
import com.example.musicapp.application.MyApplication

import com.example.musicapp.component.MainComponent
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.databinding.PlayerViewBinding
import com.example.musicapp.layout.*
import com.example.musicapp.model.Music
import com.example.musicapp.repository.MusicRepository
import com.example.musicapp.service.MediaService
import com.example.musicapp.utils.MusicStatus
import com.example.musicapp.utils.NetworkConnection
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModel
import com.example.musicapp.viewmodel.PlaylistViewModel.PlaylistViewModelFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

import com.google.android.material.navigation.NavigationBarView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    lateinit var mediaService : MediaService
    private  var isServiceConnected : Boolean = false
    lateinit var binding : ActivityMainBinding
    lateinit var bindingPlayerView : PlayerViewBinding

    @Inject
    lateinit var viewPagerAdapter : ViewPagerAdapter




    lateinit var component : MainComponent

    private val networkConnection: NetworkConnection by lazy {
        NetworkConnection(this)
    }

    val broadcastReceiver : BroadcastReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.getStringExtra("action_music")
            when (action){
                MusicStatus.PLAY_ACTION.toString() -> {

                }
                MusicStatus.RESUME_ACTION.toString()-> {
                    mediaService.handleActionMusic(MusicStatus.RESUME_ACTION)
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_pause_24)
                    bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                }
                MusicStatus.PAUSE_ACTION.toString() -> {
                    mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
                }
                MusicStatus.NEXT_ACTION.toString() -> {
                    mediaService.handleActionMusic(MusicStatus.NEXT_ACTION)
                }
                MusicStatus.PRE_ACTION.toString() -> {
                    mediaService.handleActionMusic(MusicStatus.PRE_ACTION)
                }
                else -> {

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if(isServiceConnected){
            unbindService(serviceConnection)
            val intent : Intent = Intent(this, MediaService::class.java)
            stopService(intent)
        }
        unregisterReceiver(broadcastReceiver)
    }
        private val serviceConnection : ServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
               val myBinder : MediaService.MediaServiceBinder = service as MediaService.MediaServiceBinder
                mediaService = myBinder.getService()
                isServiceConnected = true
                handlePlayer()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isServiceConnected = false
            }

        }

    private fun handlePlayer() {
        mediaService.player!!.addListener(object : Player.Listener{
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                // change view in playerView
                bindingPlayerView.playerToolbar.songTitle.text = mediaItem!!.mediaMetadata.title
                Glide.with(this@MainActivity).load(mediaItem.mediaMetadata.artworkUri).into(bindingPlayerView.imageView)
                bindingPlayerView.timeTwo.text = createTime(mediaService.player!!.duration.toInt())
                bindingPlayerView.mySeekbar.max = mediaService.player!!.duration.toInt()
                bindingPlayerView.mySeekbar.progress = mediaService.player!!.currentPosition.toInt()

                // change view in bottomView
                binding.layoutBottomMusic.visibility = View.VISIBLE
                binding.musicTitle.text = mediaItem!!.mediaMetadata.title
                Glide.with(this@MainActivity).load(mediaItem.mediaMetadata.artworkUri).into(binding.musicBottomImg)
                binding.authorName.text = mediaItem.mediaMetadata.artist
                binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_pause_24)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if(playbackState == ExoPlayer.STATE_READY){
                    // change view in playerView
                    bindingPlayerView.playerToolbar.songTitle.text = mediaService.player!!.currentMediaItem!!.mediaMetadata.title
                    Glide.with(this@MainActivity).load(mediaService.player!!.currentMediaItem!!.mediaMetadata.artworkUri).into(bindingPlayerView.imageView)
                    bindingPlayerView.timeTwo.text = createTime(mediaService.player!!.duration.toInt())
                    bindingPlayerView.mySeekbar.max = mediaService.player!!.duration.toInt()
                    bindingPlayerView.mySeekbar.progress = mediaService.player!!.currentPosition.toInt()
                    bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_pause_circle_24)

                    // change view in bottomView
                    binding.layoutBottomMusic.visibility = View.VISIBLE
                    binding.musicTitle.text =  mediaService.player!!.currentMediaItem!!.mediaMetadata.title
                    Glide.with(this@MainActivity).load(mediaService.player!!.currentMediaItem!!.mediaMetadata.artworkUri).into(binding.musicBottomImg)
                    binding.authorName.text = mediaService.player!!.currentMediaItem!!.mediaMetadata.artist
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_pause_24)

                    // update progress time and progress bar
                    updatePLayerPositionProgress()

                    // player view events
                    playerViewEvents()
                }
                if(playbackState == ExoPlayer.STATE_ENDED){
                    binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
                }

            }

        })




    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        component = (application as MyApplication).component.getMainCompent().create(this)
        component.inject(this)
        binding.viewPager2.adapter = viewPagerAdapter
        bindingPlayerView = binding.playerView
        //register broadcast
        registerReceiver(broadcastReceiver, IntentFilter("music"))

        val intent : Intent = Intent(this, MediaService::class.java)
        bindService(intent,serviceConnection, BIND_AUTO_CREATE)



        binding.bottomNavigation.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id = item.itemId
                if(id == R.id.bottom_home){
                    binding.viewPager2.setCurrentItem(0)
                }
                else if(id == R.id.bottom_search){
                    binding.viewPager2.setCurrentItem(1)
                }
                else if(id == R.id.bottom_menu){
                    binding.viewPager2.setCurrentItem(2)
                }
                else if(id == R.id.bottom_profile){
                    binding.viewPager2.setCurrentItem(3)
                }
                return true
            }

        })

        binding.viewPager2.setUserInputEnabled(false);


        // swiping viewpager for bottom navigation
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        binding.bottomNavigation.menu.findItem(R.id.bottom_home).isChecked = true
                    }
                    1 -> {
                        binding.bottomNavigation.menu.findItem(R.id.bottom_search).isChecked = true
                    }
                    2 -> {
                        binding.bottomNavigation.menu.findItem(R.id.bottom_menu).isChecked = true
                    }
                    3 -> {
                        binding.bottomNavigation.menu.findItem(R.id.bottom_profile).isChecked = true
                    }

                }
            }
        })
        // register network Connection

        networkConnection.observe(this) { isConnected ->
            if(isConnected){
                binding.internetWrapper.visibility = View.GONE
            }
            else{
                binding.internetWrapper.visibility = View.VISIBLE
            }
        }


        addEvents()
    }


    override fun onPause() {
        super.onPause()

    }
    @SuppressLint("ClickableViewAccessibility")
    private fun addEvents() {
        // handle play or pause music
        binding.hanleStartMusicBottom.setOnClickListener {
            if(mediaService.player!!.isPlaying){
                mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
            }
            else{
                mediaService.handleActionMusic(MusicStatus.RESUME_ACTION)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_pause_24)
                bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_pause_circle_24)
            }
        }

        //handle next music
        binding.handleNextMusicBottom.setOnClickListener {
            if(mediaService.player!!.isPlaying){
               mediaService.handleActionMusic(MusicStatus.NEXT_ACTION)
            }
        }
        // handle pre music
        binding.handlePreMusicBottom.setOnClickListener {
            if(mediaService.player!!.isPlaying){
                mediaService.handleActionMusic(MusicStatus.PRE_ACTION)
            }
        }
        // handling with bottom music
        binding.layoutBottomMusic.setOnClickListener {
            binding.playerView.playerView.visibility = View.VISIBLE
            binding.bottomNavigation.visibility = View.GONE

        }
        // handle close player View
        bindingPlayerView.playerToolbar.backBtn.setOnClickListener {
            binding.playerView.playerView.visibility = View.GONE
            binding.bottomNavigation.visibility = View.VISIBLE
        }
        // handle open bottom setting view
        binding.playerView.playerToolbar.menuSettingMusic.setOnClickListener {
            MusicSettingSheet().show(supportFragmentManager,"MusicSettingSheet")
        }



    }



    private fun updatePLayerPositionProgress() {
       Handler().postDelayed(Runnable {
           kotlin.run {
                if(mediaService.player!!.isPlaying){
                    bindingPlayerView.mySeekbar.progress = mediaService.player!!.currentPosition.toInt()
                    bindingPlayerView.timeOne.text = createTime(mediaService.player!!.currentPosition.toInt())
                }
               updatePLayerPositionProgress()
           }
       },1000)
    }

    private fun playerViewEvents() {
        bindingPlayerView.mySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaService.player!!.seekTo(seekBar!!.progress.toLong())
            }

        })
        bindingPlayerView.playButton.setOnClickListener {
            if(mediaService.player!!.isPlaying){
                mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
                bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_play_arrow_24)

            }
            else{
                mediaService.handleActionMusic(MusicStatus.RESUME_ACTION)
                bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_pause_circle_24)
                binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_pause_24)

            }
        }

        bindingPlayerView.btnPlayNext.setOnClickListener {
            mediaService.handleActionMusic(MusicStatus.NEXT_ACTION)
        }
        bindingPlayerView.btnPlayPre.setOnClickListener {
            mediaService.handleActionMusic(MusicStatus.PRE_ACTION)
        }

        bindingPlayerView.repeatBtn.setOnClickListener {
            if(mediaService.player!!.repeatMode == ExoPlayer.REPEAT_MODE_OFF){
                bindingPlayerView.repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24_active)
            }
            else{
                bindingPlayerView.repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24)
            }
            mediaService.handleActionMusic(MusicStatus.REPEAT_MODE)

        }
        bindingPlayerView.randomBtn.setOnClickListener {
            if(mediaService.player!!.shuffleModeEnabled){
                binding.playerView.randomBtn.setImageResource(R.drawable.ic_baseline_shuffle_24)
            }
            else{
                binding.playerView.randomBtn.setImageResource(R.drawable.ic_baseline_shuffle_24_active)
            }
            mediaService.handleActionMusic(MusicStatus.RANDOM_MODE)
        }
    }

    public fun startMusicFromService(position : Int,dataList: MutableList<Music>){
        // add MediaItem

        val mediaItems : ArrayList<MediaItem> = ArrayList()
        for(music in dataList){
            val mediaItem = MediaItem.Builder()
                .setUri(music.src_music)
                .build()
            mediaItems!!.add(mediaItem)
        }

        // open service
        val intent : Intent = Intent(this, MediaService::class.java)
        var bundle = bundleOf(
            "song" to dataList,
            "pos" to position
        )
        intent.putExtras(bundle)

        // foreground service
        startService(intent)
    }


    public fun logout(){
        val intent = Intent(this,SplashScreen::class.java)
        startActivity(intent)
    }

    public fun getMv(music: String){
        val intent = Intent(this,WebviewScreen::class.java)
        intent.putExtra("music",music)
        if(mediaService.player!!.isPlaying){
            mediaService.handleActionMusic(MusicStatus.PAUSE_ACTION)
            bindingPlayerView.playButton.setImageResource(R.drawable.ic_baseline_play_circle_24)
            binding.hanleStartMusicBottom.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        }
        startActivity(intent)
    }

    public fun createTime(duration : Int) : String{
        var time : String = ""
        var min = duration/1000/60
        var sec = duration/1000%60

        time = "$time$min:"
        if(sec < 10) {
            time += "0"
        }
        time+=sec
        return time
    }




}