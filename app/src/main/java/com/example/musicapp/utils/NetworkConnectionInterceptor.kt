package com.example.musicapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.musicapp.application.MyApplication
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(val app : MyApplication) : Interceptor {
    private val applicationContext = app.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isInternetAvailable()){
            throw NoInternetException("No internet connection")
        }
        return chain.proceed(chain.request())
    }
    private fun isInternetAvailable(): Boolean{
        val connectivityManager = applicationContext.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
    }
}