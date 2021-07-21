package com.sample.vkoelassign.data.network

/**
 *
 * Purpose – Pojo class For login validation
 *
 * @author Rishabh Gupta
 *
 * */
data class LoginFormDataModel(
    val mobileNum: String,
    val password: String,
    val name: String,
    var isValid: Boolean = true,
    var error: String = ""
)
