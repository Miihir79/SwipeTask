package com.mihir.swipetask.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.util.Calendar

const val BASE_URL = "https://app.getswipe.in/api/public/"
const val API_FAILED = "API_FAILED"
const val API_SUCCESS = "API_SUCCESS"
const val NO_NETWORK = "NO_NETWORK"

object Utils {
    /**
     * Function used to get the Greeting message from the hour of the day
     */
    fun greetUser(): String {
        val c = Calendar.getInstance()
        val greet = when (c.get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 15..20 -> "Good Evening"
            else -> "Good Night"
        }
        return greet
    }

    /**
     * Function that returns true if network is available otherwise returns false
     */
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        val isNetworkConnected = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return isNetworkConnected
    }

}