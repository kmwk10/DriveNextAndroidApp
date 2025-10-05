package com.example.drivenextapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class ConnectivityLiveData(private val context: Context) : LiveData<Boolean>() {

    private val cm: ConnectivityManager? =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            // на некоторых устройствах onLost может вызываться позже, поэтому проверяем текущее состояние
            postValue(isCurrentlyConnected())
        }
    }

    override fun onActive() {
        super.onActive()
        postValue(isCurrentlyConnected())
        try {
            val request = NetworkRequest.Builder().build()
            cm?.registerNetworkCallback(request, callback)
        } catch (ex: Exception) {
            // fallback: если регистрация провалилась — оставляем текущее значение
        }
    }

    override fun onInactive() {
        super.onInactive()
        try {
            cm?.unregisterNetworkCallback(callback)
        } catch (_: Exception) { }
    }

    private fun isCurrentlyConnected(): Boolean {
        val nw = cm?.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(nw) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                caps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
    }

}
