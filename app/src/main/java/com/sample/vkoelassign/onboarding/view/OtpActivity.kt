package com.sample.vkoelassign.onboarding.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ActivityOtpBinding
import com.sample.vkoelassign.home.view.MainActivity
import com.sample.vkoelassign.utility.Constants.PHONE_NUM
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort
import java.util.concurrent.TimeUnit


/**
 *
 * Purpose – onBoarding Otp Screen
 *
 * @author Rishabh Gupta
 *
 * */
class OtpActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOtpBinding
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.idBtnGetOtp -> {
                binding.idEdtOtp.setText("")
                if (!Utils.isValidMobile(binding.idEdtPhoneNumber.getText().toString()))
                    toastShort("Please enter a valid phone number")
                else {
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
        if (intent != null && intent.extras != null && !intent.extras?.getString(PHONE_NUM)
                .isNullOrEmpty()
        ) {
            val bundle = intent.extras
            val phoneNum = bundle!!.getString(PHONE_NUM)
            if (phoneNum != getString(R.string.txt_pay_dummy_phone_no))
                binding.idEdtPhoneNumber.setText(phoneNum)
        }
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
                binding.progressBar.visibility = View.GONE
                val i = Intent(this@OtpActivity, MainActivity::class.java)
                startActivity(i)
                finish()
                //saveUserInfo("","","")
            } else {
                binding.progressBar.visibility = View.GONE
                // if the code is not correct then we are
                // displaying an error message to the user.
                toastShort("${task.exception?.message}")
            }
        }
    }


    private fun sendVerificationCode(number: String) {
        // this method is used for getting
        // OTP on user phone number.
        binding.progressBar.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
                verificationId = s
            }

            // this method is called when user
            // receive OTP from Firebase.
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // below line is used for getting OTP code
                // which is sent in phone auth credentials.
                binding.progressBar.visibility = View.GONE
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    binding.idEdtOtp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // displaying error message with firebase exception.
                binding.progressBar.visibility = View.GONE
                toastShort("${e.message}")
            }
        }

    // below method is use to verify code from Firebase.
    private fun verifyCode(code: String) {
        binding.progressBar.visibility = View.VISIBLE
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun saveUserInfo(fullName: String, userName: String, email: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId!!
        userMap["bio"] = "Hey man whats going on"
        userMap["email"] = "xyz@gmail.com"
        userMap["fullName"] = "ajay gupta"
        userMap["userName"] = "ajaygupta"
        userMap["image"] =
            "https://firebasestorage.googleapis.com/v0/b/payassignment-df7db.appspot.com/o/Default%20Images%2Flaptop_2.jpg?alt=media&token=d41f3103-f463-4afc-b183-53182cf5f686"

        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toastShort("Account created successfully")

                    //makeUserFollowItself(currentUserId)

                    val intent = Intent(this@OtpActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                } else {
                    //binding.progressBar.visibility = View.GONE
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    toastShort("${task.exception?.message}")
                    FirebaseAuth.getInstance().signOut()
                }
            }
    }

    /**
     * This method will allow user to follow themselves
     *
     * In user feed or User Timeline all post(to which he/she is following) along with this particular user post will be visible
     */
    private fun makeUserFollowItself(currentUserId: String) {
        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(currentUserId)
            .child("Following").child(currentUserId)
            .setValue(true)

    }
}