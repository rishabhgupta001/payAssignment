package com.sample.vkoelassign.ui.view.adapter

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ActivityLoginBinding
import com.sample.vkoelassign.databinding.ActivityOtpBinding
import com.sample.vkoelassign.ui.view.MainActivity
import com.sample.vkoelassign.utility.toastShort
import java.util.concurrent.TimeUnit


class OtpActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOtpBinding
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.idBtnGetOtp -> {
                if (TextUtils.isEmpty(binding.idEdtPhoneNumber.getText().toString())) {
                    toastShort("Please enter a valid phone number")
                } else {
                    val phone = "+91" + binding.idEdtPhoneNumber.getText().toString()
                    sendVerificationCode(phone)
                }
            }
            R.id.idBtnVerify -> {
                if (TextUtils.isEmpty(binding.idEdtOtp.getText().toString())) {
                    toastShort("Please enter OTP")
                } else {
                    verifyCode(binding.idEdtOtp.getText().toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance()
        // setting onclick listener for generate OTP button.
        binding.idBtnGetOtp.setOnClickListener(this)
        binding.idBtnVerify.setOnClickListener(this)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // if the code is correct and the task is successful
                // we are sending our user to new activity.
                val i = Intent(this@OtpActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            } else {
                // if the code is not correct then we are
                // displaying an error message to the user.
                Toast.makeText(
                    this@OtpActivity,
                    task.exception?.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }


    private fun sendVerificationCode(number: String) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            this,
            mCallBack
        )
    }

    // callback method is called on Phone auth provider.
    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            // below method is used when
            // OTP is sent from Firebase
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    binding.idEdtOtp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                toastShort("${e.message}")
            }
        }

    // below method is use to verify code from Firebase.
    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }
}