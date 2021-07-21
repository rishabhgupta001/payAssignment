package com.sample.vkoelassign.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.databinding.FragmentPostDetailBinding
import com.sample.vkoelassign.data.network.Comment
import com.sample.vkoelassign.data.network.Post
import com.sample.vkoelassign.data.network.User
import com.sample.vkoelassign.ui.view.adapter.CommentAdapter
import com.sample.vkoelassign.utility.Utils

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private lateinit var data: Post
    private var commentAdapter: CommentAdapter? = null
    private var commentList: MutableList<Comment>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        arguments?.let {
            data = PostDetailFragmentArgs.fromBundle(it).post!!

            (activity as MainActivity).binding.toolbarTitle.text = data.screenTitle

            //binding.descriptionTextView.text = data.postDescription
            Utils.showFadeInAnimOnText(requireContext(), binding.descriptionTextView, data.postDescription)
            Utils.setImage(binding.postImgView, data.postImage)
            setData(binding, data)
        }

        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        val linearLayoutManager = LinearLayoutManager(context)
        binding.commentsRecyclerView.layoutManager = linearLayoutManager
        commentList = ArrayList()
        commentAdapter = context?.let { CommentAdapter(it, commentList as ArrayList<Comment>) }
        binding.commentsRecyclerView.adapter = commentAdapter

        fetchComments()
    }

    private fun setData(itemBinding: FragmentPostDetailBinding, data: Post) {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
            .child(data.postPublisher)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)
                    //itemBinding.profileNameTextView.text = user?.userName
                    Utils.showSlideDownAnimOnText(requireContext(), itemBinding.profileNameTextView, user?.userName!!)
                    //itemBinding.publisher.text = user?.fullName
                    Utils.showSlideDownAnimOnText(requireContext(), itemBinding.publisher, user.fullName)
                    Utils.setImage(itemBinding.profileImgView, user.image)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun fetchComments() {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(data.postId)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    commentList?.clear()

                    for (item in p0.children) {
                        val comment = item.getValue(Comment::class.java)
                        commentList?.add(comment!!)
                    }
                    commentAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}