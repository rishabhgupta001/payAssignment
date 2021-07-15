package com.sample.vkoelassign.application

import android.app.Application

class MyApplication : Application() {
    companion object {
        lateinit var mInstance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        //creating instance of MyApplication class
        mInstance = this
    }

    @Synchronized
    fun getInstance(): MyApplication {
        return mInstance
    }
}