package com.sample.vkoelassign.onboarding.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sample.vkoelassign.data.network.LoginFormDataModel
import com.sample.vkoelassign.utility.Utils


/**
 *
 * Purpose – ViewModel for Login screen validations
 *
 * @author Rishabh Gupta
 *
 * */
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