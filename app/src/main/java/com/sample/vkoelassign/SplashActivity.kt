package com.sample.vkoelassign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.sample.vkoelassign.databinding.ActivitySplashBinding
import com.sample.vkoelassign.onboarding.view.LoginActivity
import com.sample.vkoelassign.home.view.MainActivity
import com.sample.vkoelassign.utility.Utils


/**
 *
 * Purpose – Launcher Activity of this app
 *
 * @author Rishabh Gupta
 *
 * */
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        Utils.showFadeInAnimOnText(
            this@SplashActivity,
            binding.welcomeTxtView,
            getString(R.string.txt_welc_to_xapp)
        )
        Handler().postDelayed(object : Runnable {
            override fun run() {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    Utils.launchNewActivity(this@SplashActivity, mainIntent, true)
                    finish()
                } else {
                    //val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                    val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                    //val mainIntent = Intent(this@SplashActivity, OtpActivity::class.java)
                    Utils.launchNewActivity(this@SplashActivity, mainIntent, true)
                    finish()
                }
            }
        }, 1000)
    }
}