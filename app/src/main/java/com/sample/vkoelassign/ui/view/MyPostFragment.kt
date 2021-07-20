package com.sample.vkoelassign.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.FragmentMyFeedBinding
import com.sample.vkoelassign.databinding.FragmentMyPostBinding
import com.sample.vkoelassign.network.Post
import com.sample.vkoelassign.ui.view.adapter.MyFeedAdapter
import com.sample.vkoelassign.utility.toastShort
import java.util.*
import kotlin.collections.ArrayList

class MyPostFragment : Fragment() {
    private lateinit var binding: FragmentMyPostBinding
    private var postAdapter: MyFeedAdapter? = null
    private var postList: MutableList<Post>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding.progressBar.visibility = View.VISIBLE
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.userPostRecyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        postAdapter = context?.let { MyFeedAdapter(it, postList as ArrayList<Post>) }
        binding.userPostRecyclerView.adapter = postAdapter

        fetchCurrentUserPost()
    }

    private fun fetchCurrentUserPost() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    (postList as ArrayList<Post>).clear()

                    for (item in dataSnapshot.children) {
                        val post = item.getValue(Post::class.java)
                        if (post?.postPublisher == FirebaseAuth.getInstance().currentUser?.uid) {
                            (postList as ArrayList<Post>).add(post!!)
                        }
                        postAdapter?.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}