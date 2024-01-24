package com.example.musicapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.application.MyApplication
import com.example.musicapp.broadcast.MyReceiver
import com.example.musicapp.model.Music
import com.example.musicapp.utils.MusicStatus
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


class MediaService : Service(),MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{



     val player: ExoPlayer? by lazy {
         ExoPlayer.Builder(this).build()
     }
    var isPLaying : Boolean = false
    companion object {
        const val ACTION_PAUSE = 1;
        const val ACTION_PLAY = 2;

    }
    inner class MediaServiceBinder : Binder(){
        fun getService() : MediaService {
           return this@MediaService
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
       return MediaServiceBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }
    override fun onCreate() {
        super.onCreate()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var bunble = intent?.extras
       val music = bunble?.get("song")
        val position = bunble?.get("pos")
        val mediaItems : ArrayList<MediaItem> = ArrayList()
        for(data in music as List<Music>){
            val mediaItem = MediaItem.Builder()
                .setMediaId(data._id.toString())
                .setUri(data.src_music)
                .setMediaMetadata(getMetaData(data))
                .build()
            mediaItems!!.add(mediaItem)
        }

        player!!.setMediaItems(mediaItems,position as Int,0)
        startMusic()
        return START_NOT_STICKY
    }
    private fun getMetaData(music: Music): MediaMetadata {
        return MediaMetadata.Builder()
            .setTitle(music.name_music)
            .setArtist(music.name_singer)
            .setArtworkUri(music.image_music!!.toUri())
            .build()
    }
     fun startMusic() {
         if(isPLaying){
             player!!.pause()

         }
         // Start the playback.
         player!!.prepare()
         player!!.play()
         isPLaying = true
         sendNotification()
    }

    private fun pauseMusic(){
        if(player != null && isPLaying){
            player!!.pause()
            isPLaying = false
            sendNotification()
        }
    }
    private fun resumeMusic(){
        if(player != null && !isPLaying){
            player!!.play()
            isPLaying = true
            sendNotification()
        }
    }
    private fun seekToNextPlaylist(){
        if(isPLaying && player!!.hasNextMediaItem()){
            player!!.seekToNext()
            sendNotification()
        }
    }

    private fun seekToPreviousPLaylist(){
        if(isPLaying && player!!.hasPreviousMediaItem()){
            player!!.seekToPrevious()
            sendNotification()
        }
    }
    private fun setRepeatMode(){
        if(player!!.repeatMode == ExoPlayer.REPEAT_MODE_OFF){
            player!!.repeatMode = ExoPlayer.REPEAT_MODE_ALL
        }
       else{
            player!!.repeatMode = ExoPlayer.REPEAT_MODE_OFF
        }
    }
    private fun setRandomMode(){
        player!!.shuffleModeEnabled = !player!!.shuffleModeEnabled
    }
    public fun handleActionMusic(x : MusicStatus = MusicStatus.PLAY_ACTION){
        when(x){
            MusicStatus.RESUME_ACTION -> {
                resumeMusic()
            }
            MusicStatus.PAUSE_ACTION -> {
                pauseMusic()
            }
            MusicStatus.STOP_ACTION -> {
                stopSelf()
            }
            MusicStatus.NEXT_ACTION -> {
                seekToNextPlaylist()
            }
            MusicStatus.PRE_ACTION -> {
                seekToPreviousPLaylist()
            }
            MusicStatus.REPEAT_MODE -> {
                setRepeatMode()
            }
            MusicStatus.RANDOM_MODE -> {
                setRandomMode()
            }

            else -> {
                stopSelf()
            }
        }

    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer!!.start()
    }
    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int): Boolean {
        return false
    }
    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun sendNotification() {
        // intent comback view
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        val mediaSession : MediaSessionCompat = MediaSessionCompat(this,"tag")
        val notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this,
            MyApplication.CHANNEL_MUSIC)
            .setSmallIcon(R.drawable.ic_baseline_audiotrack_24)
            .setContentTitle(player!!.mediaMetadata.title)
            .setContentText(player!!.mediaMetadata.artist)
            .setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1).setMediaSession(mediaSession.sessionToken))
            .setSound(null)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0,1,2)
                .setMediaSession(mediaSession.getSessionToken()))
        notificationBuilder.setSilent(true)
        applyImageUrl(notificationBuilder,player!!.mediaMetadata.artworkUri.toString())
        if(isPLaying){
            notificationBuilder
                .addAction(R.drawable.ic_baseline_skip_previous_24,"previous",getPendingIntent(this,MusicStatus.PRE_ACTION))
                .addAction(R.drawable.ic_baseline_pause_circle_24,"Pause",getPendingIntent(this,MusicStatus.PAUSE_ACTION))
                .addAction(R.drawable.ic_baseline_skip_next_24,"Next",getPendingIntent(this,MusicStatus.NEXT_ACTION))
        }
        else{
            notificationBuilder
                .addAction(R.drawable.ic_baseline_skip_previous_24,"previous",getPendingIntent(this,MusicStatus.PRE_ACTION))
                .addAction(R.drawable.ic_baseline_play_circle_24,"Play",getPendingIntent(this,MusicStatus.RESUME_ACTION))
                .addAction(R.drawable.ic_baseline_skip_next_24,"Next",getPendingIntent(this,MusicStatus.NEXT_ACTION))
        }
        val notification = notificationBuilder.build()
        val managerCompat : NotificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        managerCompat.notify(1,notification)
    }

    fun applyImageUrl(
        builder: NotificationCompat.Builder,
        imageUrl: String
    ) = runBlocking {
        val url = URL(imageUrl)

        withContext(Dispatchers.IO) {
            try {
                val input = url.openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
        }?.let { bitmap ->
            builder.setLargeIcon(bitmap)
        }
    }

    private fun getPendingIntent(context: Context, action: MusicStatus): PendingIntent? {
        val intent = Intent(this,MyReceiver::class.java)
        intent.setAction(action.toString())
        return PendingIntent.getBroadcast(context.applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }



}