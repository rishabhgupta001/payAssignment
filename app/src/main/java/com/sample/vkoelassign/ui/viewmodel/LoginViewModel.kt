package com.sample.vkoelassign.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sample.vkoelassign.R
import com.sample.vkoelassign.application.MyApplication
import com.sample.vkoelassign.network.LoginFormDataModel
import com.sample.vkoelassign.ui.view.MainActivity
import com.sample.vkoelassign.utility.Pref
import com.sample.vkoelassign.utility.Utils
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val loginFormLiveData: MutableLiveData<LoginFormDataModel> = MutableLiveData()

    //Login Screen
    fun validate(dataModel: LoginFormDataModel) {
        when {
            !Utils.isValidMobile(dataModel.mobileNum) -> {
                dataModel.isValid = false
                dataModel.error = "PhoneNum must not be empty"
            }

            dataModel.password.isNullOrEmpty() -> {
                dataModel.isValid = false
                dataModel.error = "Password must not be empty"
            }
        }
        loginFormLiveData.value = dataModel
    }
}