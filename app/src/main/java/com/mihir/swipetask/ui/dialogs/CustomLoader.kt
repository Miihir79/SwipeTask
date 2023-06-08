package com.mihir.swipetask.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.mihir.swipetask.R

class CustomLoader(private val activity: Activity) {

    private val loader by lazy {
        AlertDialog.Builder(activity)
            .setView(activity.layoutInflater.inflate(R.layout.custom_loader, null))
            .setCancelable(false)
            .create()
    }

    fun startLoading() {
        loader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loader.show()
    }

    fun stopLoading() {
        loader.dismiss()
    }

}