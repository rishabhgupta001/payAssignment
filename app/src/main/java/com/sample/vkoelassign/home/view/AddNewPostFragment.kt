package com.sample.vkoelassign.home.view

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.FragmentAddNewPostBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.UploadTask
import com.sample.vkoelassign.utility.toastShort


class AddNewPostFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddNewPostBinding
    private var myUrl = ""
    private var imageUri: Uri? = null
    private var storageProfilePicRef: StorageReference? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.save_new_post_btn -> {
                    uploadImage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    /**
     * View Initialization
     */
    private fun init() {
        binding.saveNewPostBtn.setOnClickListener(this)
        storageProfilePicRef = FirebaseStorage.getInstance().reference.child("Post Pictures")

        startCrop()
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            binding.postImgView.setImageURI(uriContent)
            imageUri = uriContent
        } else {
            // an error occurred
            val exception = result.error
            context?.toastShort("${exception}")
        }
    }

    private fun startCrop() {
        // start picker to get image for cropping and then use the image in cropping activity
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )

        // start cropping activity for pre-acquired image saved on the device and customize settings
        cropImage.launch(
            options(uri = imageUri) {
                setGuidelines(CropImageView.Guidelines.ON)
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
        )
    }

    private fun uploadImage() {
        when {
            imageUri == null ->
                context?.toastShort("Please select image first.")

            TextUtils.isEmpty(
                binding.descriptionPostEditText.getText().toString()
            ) -> context?.toastShort("Please enter description.")

            else -> {
                val progressDialog = ProgressDialog(context)
                progressDialog.setTitle("Adding new post")
                progressDialog.setMessage("Please wait, we are updating your post...")
                progressDialog.show()

                val fileRef =
                    storageProfilePicRef!!.child(System.currentTimeMillis().toString() + ".jpg")

                val uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(imageUri!!)

                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri> { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        myUrl = downloadUrl.toString()

                        val ref = FirebaseDatabase.getInstance().reference.child("Posts")
                        val postId = ref.push().key!!

                        val postMap = HashMap<String, Any>()
                        postMap["postImage"] = myUrl
                        postMap["postId"] = postId
                        postMap["postDescription"] = binding.descriptionPostEditText.text.toString()
                        postMap["postPublisher"] = FirebaseAuth.getInstance().currentUser?.uid!!

                        ref.child(postId).updateChildren(postMap)

                        context?.toastShort("Post Information has been updated successfully.")

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                    }
                })
            }
        }
    }
}