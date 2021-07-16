package com.sample.vkoelassign.network

data class LoginFormDataModel(
    val mobileNum: String,
    val password: String,
    val name: String,
    var isValid: Boolean = true,
    var error: String = ""
)
