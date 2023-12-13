package com.example.musicapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.example.musicapp.databinding.ActivitySplashScreenBinding
import com.example.musicapp.share.sharePreferencesUtils

@Suppress("UNREACHABLE_CODE")
class SplashScreen : AppCompatActivity() {
    lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.myLooper()!!).postDelayed({
            if(sharePreferencesUtils.isSharedPreferencesExist(this,"USER","TOKEN_VALUE")){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this,LoginSrcreen::class.java)
                startActivity(intent)
            }
        },500)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
}