package com.example.chekhebar.core.network

import android.content.Context
import android.net.ConnectivityManager
import com.example.chekhebar.MapApp

class NetworkHandler constructor(private val context: MapApp) {
    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}