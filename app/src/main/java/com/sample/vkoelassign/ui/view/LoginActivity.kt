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
import android.provider.SyncStateContract
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ActivityLoginBinding
import com.sample.vkoelassign.utility.Constants
import com.sample.vkoelassign.utility.Constants.PERMISSIONS_REQUEST_READ_CONTACTS


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    val PICK_CONTACT = 1

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.phone_numb_editText -> {
                phoneNumberPicker()
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
        binding.phoneNumbEditText.setOnClickListener(this)
        // Read and show the contacts
        showContacts()
    }

    private fun showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            val contacts: List<String> = getContactNames()
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contacts)
            lstNames.setAdapter(adapter)
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
                showContacts()
            } else {
                Toast.makeText(
                    this,
                    "Until you grant the permission, we canot display the names",
                    Toast.LENGTH_SHORT
                ).show()
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
                            val phones: Cursor? = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                null, null
                            )
                            phones?.moveToFirst()
                            val cNumber = phones?.getString(phones.getColumnIndex("data1"))
                            System.out.println("number is:" + cNumber)
                            binding.phoneNumbEditText.setText(cNumber)
                        }
                        var name: String =
                            c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    }
                }
            }
        }
    }
}