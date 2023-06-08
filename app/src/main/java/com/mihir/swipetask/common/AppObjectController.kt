package com.mihir.swipetask.common

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mihir.swipetask.network.NetworkService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// A class which helps us keep all the objects in check
class AppObjectController {

    companion object {
        private val gson: Gson by lazy { GsonBuilder().create() }

        lateinit var applicationContext: Application

        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
        }

        val service: NetworkService by lazy { retrofit.create(NetworkService::class.java) }

    }
}