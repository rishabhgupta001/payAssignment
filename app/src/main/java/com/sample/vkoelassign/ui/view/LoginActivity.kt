package com.sample.vkoelassign.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ActivityLoginBinding
import com.sample.vkoelassign.data.network.LoginFormDataModel
import com.sample.vkoelassign.ui.viewmodel.LoginViewModel
import com.sample.vkoelassign.utility.Constants.PERMISSIONS_REQUEST_READ_CONTACTS
import com.sample.vkoelassign.utility.Constants.PHONE_NUM
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    private val PICK_CONTACT = 1
    private var isReadPermissionGranted = false
    private val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private var userName: String = ""
    private var mAuth: FirebaseAuth? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.contacts_btn -> {
                if (isReadPermissionGranted)
                    phoneNumberPicker()
                else
                    gettingContactsPermission()
            }
            R.id.login_btn -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.loginBtn.isEnabled = false
                Handler().postDelayed({
                    binding.loginBtn.isEnabled = true
                    val dataModel = LoginFormDataModel(
                        binding.phoneNumbEditText.text.toString(),
                        binding.passEditText.text.toString(), userName
                    )
                    viewModel.validate(dataModel)
                }, 100)
            }

            R.id.login_with_mobile_no_otp -> {
                val mainIntent = Intent(this@LoginActivity, OtpActivity::class.java)
                Utils.launchNewActivity(this@LoginActivity, mainIntent, true)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        mAuth = FirebaseAuth.getInstance()
        binding.contactsBtn.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        binding.loginWithMobileNoOtp.setOnClickListener(this)
        // Read and show the contacts
        gettingContactsPermission()
        observeLoginFormValidationResponseData()
        //test credentials
        binding.phoneNumbEditText.setText(getString(R.string.txt_pay_dummy_phone_no))
        binding.passEditText.setText(getString(R.string.txt_pay_passwrd))
    }

    private fun gettingContactsPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            isReadPermissionGranted = true
            Log.d("LoginActivity", "permission is already granted")
            //toastShort(getString(R.string.txt_permission_granted))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                gettingContactsPermission()
                isReadPermissionGranted = true
            } else {
                isReadPermissionGranted = false
                toastShort(getString(R.string.txt_until_you))
                gettingContactsPermission()
            }
        }
    }

    private fun phoneNumberPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT)
        binding.phoneNumbEditText.setText("")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_CONTACT -> {
                if (resultCode == Activity.RESULT_OK) {
                    val contactData: Uri? = data?.data
                    val c: Cursor = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        val id: String =
                            c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        val hasPhone: String =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equals("1", ignoreCase = true)) {
                            Log.d("LoginActivity", "hasPhone  if: ${hasPhone}")
                            val phones: Cursor? = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null
                            )
                            phones?.moveToFirst()
                            val cNumber = phones?.getString(phones.getColumnIndex("data1"))
                            Log.d("LoginActivity", "number is:${cNumber}")
                            binding.phoneNumbEditText.setText(Utils.getValidMobileNumber(cNumber!!))
                            binding.passEditText.setText(getString(R.string.txt_pay_passwrd))
                        } else {
                            Log.d("LoginActivity", "hasPhone  else: ${hasPhone}")
                            toastShort(getString(R.string.txt_select_diff_user))
                        }
                        userName =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    }
                }
            }
        }
    }

    /**
     * Method to Check Validation of Form Data Filled by User
     */
    fun observeLoginFormValidationResponseData() {
        viewModel.loginFormLiveData.observe(this, Observer {
            when (it?.isValid) {
                true -> {

                    if (binding.phoneNumbEditText.text.toString() == "1234567890") {
                        val emailId = "${binding.phoneNumbEditText.text.toString()}@gmail.com"
                        val presetPass = getString(R.string.txt_pay_passwrd)

                        //logging in the user
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId, presetPass)
                            .addOnCompleteListener(this, { task ->
                                binding.progressBar.visibility = View.GONE
                                //if the task is successfull
                                if (task.isSuccessful) {
                                    //start the profile activity
                                    val mainIntent =
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    Utils.launchNewActivity(this@LoginActivity, mainIntent, true)
                                    finish()

                                    //saveUserInfo("", "", "")

                                } else {
                                    binding.progressBar.visibility = View.GONE
                                    // displaying an error message to the user.
                                    toastShort("${task.exception?.message}")
                                }
                            })
                    } else {
                        val mainIntent = Intent(this@LoginActivity, OtpActivity::class.java)
                        mainIntent.putExtra(PHONE_NUM, binding.phoneNumbEditText.text.toString())
                        Utils.launchNewActivity(this@LoginActivity, mainIntent, true)
                        finish()
                    }
                }
                false -> {
                    binding.progressBar.visibility = View.GONE
                    toastShort(it.error)
                }
            }
        })
    }

    private fun saveUserInfo(fullName: String, userName: String, email: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserId!!
        userMap["bio"] = "Hey I am tester"
        userMap["email"] = "tester@gmail.com"
        userMap["fullName"] = "tester qa"
        userMap["userName"] = "testerQa"
        userMap["image"] =
            "https://firebasestorage.googleapis.com/v0/b/payassignment-df7db.appspot.com/o/Default%20Images%2Flaptop_2.jpg?alt=media&token=d41f3103-f463-4afc-b183-53182cf5f686"

        userRef.child(currentUserId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    toastShort("Account created successfully")

                    //makeUserFollowItself(currentUserId)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
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
}