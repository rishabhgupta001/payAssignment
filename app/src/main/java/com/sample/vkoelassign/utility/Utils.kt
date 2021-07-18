package com.sample.vkoelassign.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sample.vkoelassign.R
import com.sample.vkoelassign.SplashActivity
import com.sample.vkoelassign.ui.view.LoginActivity

object Utils {
    /**
     * Method to set Image with placeholder
     */
    fun setImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView).load(imageUrl)
            .override(200, 200)
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

    /**
     * returns true if mobile number provided is correct
     */
    fun isValidMobile(mobile: String?): Boolean {
        return !mobile.isNullOrEmpty() && (mobile.length in 6..12) && Patterns.PHONE.matcher(
            mobile
        ).matches()
    }

    /**
     *    1. If number starts with "0" than remove "0" and return number.
    2. If number starts with "+91" than remove "+91" and return number.
    3. If number contains any special characters in between than remove that and return number
     */
    fun getValidMobileNumber(number: String): String {
        var number = number.trim()
        //checking whether mobile number starts with "0"
        if (number.startsWith("0"))
            number = number.substring(0, number.length)

        //checking whether mobile number starts with "+91"
        else if (number.startsWith("+"))
            number = number.substring(3, number.length)

        number = number.replace("[\\D]".toRegex(), "")

        Log.d("LoginActivity", "number:-  ${number.trim()}")
        return number.trim()
    }

    /**
     * Method to open New Actvity/Screen
     */
    fun launchNewActivity(activity: Activity, intent: Intent, clearStack: Boolean) {
        if (clearStack) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity)
    }

    /**
     * make and Returns Dialog
     */
    fun makeDialog(id: Int, mContext: Context): Dialog {
        val dialog = Dialog(mContext)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(id)

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window!!.getAttributes().windowAnimations = R.style.DialogTheme
        dialog.show()
        return dialog
    }
}