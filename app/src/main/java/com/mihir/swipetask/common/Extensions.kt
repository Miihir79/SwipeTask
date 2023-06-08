package com.mihir.swipetask.common

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import coil.load
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.mihir.swipetask.R

private lateinit var toast: Toast

// This file stores all the extension functions to make code concise
/**
 * Function used to show toast message: [message] Toast.LENGTH_LONG
 */
fun Context.showToastMessage(message: String) {
    if (::toast.isInitialized) {
        toast.cancel()
    }
    toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    toast.show()
}

/**
 * Function used to load the [url] to ImageView, while it is loading show a Shimmer View and show error cases as well
 */
fun ImageView.loadImg(url:String) {
    if (url.isBlank() || url.isEmpty()) {
        this.setImageResource(R.drawable.default_product_image)
        return
    }
    val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(1800)
        .setBaseAlpha(0.7f)
        .setHighlightAlpha(0.6f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()

    val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
    this.load(url) {
        placeholder(shimmerDrawable)
        fallback(R.drawable.default_product_image)
        error(R.drawable.ic_error)
    }

}