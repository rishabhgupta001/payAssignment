package com.sample.vkoelassign.utility

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.text.SimpleDateFormat
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sample.vkoelassign.R

object Utils {
    /**
     * Method to set Image with placeholder
     */
    fun setImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView).load(imageUrl)
            .override(300, 300)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.placeholder_products)
            .into(imageView)

    }

    /**
     * Method to show msg to user using Snackbar
     */
    fun showSnackBar(
        view: View,
        msg: String,
        actionName: String,
        onClickListener: View.OnClickListener
    ) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).also { snackbar ->
            snackbar.setAction(actionName) {

                onClickListener.onClick(it)
            }
        }.show()


    }


    /**
     * Method returns true is Internet is available else false
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetAvailable(context: Context?): Boolean {
        var result = false
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }


    /***
     * Method to show Fade In Animation on Texts
     */
    fun showFadeInAnimOnText(context: Context, textView: TextView, msg: String) {
        val animFadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        textView.visibility = View.VISIBLE
        textView.text = msg
        textView.startAnimation(animFadeIn)
    }

    /***
     * Method to show Slide Down Anim OnText
     */
    fun showSlideDownAnimOnText(context: Context, textView: TextView, msg: String) {
        val animFadeIn = AnimationUtils.loadAnimation(context, R.anim.slide_down_txt)
        textView.visibility = View.VISIBLE
        textView.text = msg
        textView.startAnimation(animFadeIn)
    }

    /***
     * Method to show Bounce Anim On Text
     */
    fun showBounceAnimOnText(context: Context, textView: TextView, msg: String) {
        val animFadeIn = AnimationUtils.loadAnimation(context, R.anim.bounce_txt)
        textView.visibility = View.VISIBLE
        textView.text = msg
        textView.startAnimation(animFadeIn)
    }

}