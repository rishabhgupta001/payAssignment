package com.sample.vkoelassign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sample.vkoelassign.databinding.ActivitySplashBinding
import com.sample.vkoelassign.ui.view.MainActivity
import com.sample.vkoelassign.utility.Utils

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
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
                overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity)
            }
        }, 800)
    }
}