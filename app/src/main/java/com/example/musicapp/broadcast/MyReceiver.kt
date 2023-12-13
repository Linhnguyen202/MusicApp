package com.example.musicapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.musicapp.MainActivity
import com.example.musicapp.service.MediaService

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?)  {
        val action = intent!!.action
        val intent = Intent("music")
        intent.putExtra("action_music",action.toString())
        context!!.sendBroadcast(intent)
    }

}