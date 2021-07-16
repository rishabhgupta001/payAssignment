package com.sample.vkoelassign.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ActivityLoginBinding
import com.sample.vkoelassign.utility.Constants.PERMISSIONS_REQUEST_READ_CONTACTS
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    val PICK_CONTACT = 1
    var isReadPermissionGranted = false

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.contacts_btn -> {
                if (isReadPermissionGranted)
                    phoneNumberPicker()
                else
                    gettingContactsPermission()
            }
            R.id.login_btn -> {
                val mainIntent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
                overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity)
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
        binding.contactsBtn.setOnClickListener(this)
        binding.loginBtn.setOnClickListener(this)
        // Read and show the contacts
        gettingContactsPermission()
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
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
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
                        //var name: String = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    }
                }
            }
        }
    }
}