package com.mihir.swipetask

import android.app.Application
import com.facebook.stetho.Stetho
import com.mihir.swipetask.common.AppObjectController

class SwipeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppObjectController.applicationContext = this
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}