package com.sample.vkoelassign.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.FragmentCommentsBinding
import com.sample.vkoelassign.data.network.Post
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort

/**
 *
 * Purpose – This screen provide feature to add new comments on available post
 *
 * @author Rishabh Gupta
 *
 * */
class CommentsFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentCommentsBinding
    private var itemData: Post? = null

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_btn -> {
                if(!binding.addCommentEditText.text.isNullOrEmpty())
                addComments(itemData!!)
                else
                    context?.toastShort("Please enter comment")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.addBtn.setOnClickListener(this)

        arguments?.let {
            itemData = CommentsFragmentArgs.fromBundle(it).post
            Utils.setImage(binding.detailImgView, itemData?.postImage!!)
        }

    }

    private fun addComments(data: Post) {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(data.postId)

        val commentsMap = HashMap<String, Any>()
        commentsMap["comment"] = binding.addCommentEditText.text.toString()
        commentsMap["publisher"] = FirebaseAuth.getInstance().currentUser?.uid!!
        commentsRef.push().setValue(commentsMap)

        binding.addCommentEditText.text.clear()
    }


}