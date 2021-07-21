package com.sample.vkoelassign.home.view

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
import com.sample.vkoelassign.data.network.Post
import com.sample.vkoelassign.home.view.adapter.MyFeedAdapter
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort

/**
 *
 * Purpose – This screen code for MyFeedScreen
 *
 * @author Rishabh Gupta
 *
 * */
class MyFeedFragment : Fragment() {
    private lateinit var binding: FragmentMyFeedBinding
    private var feedAdapter: MyFeedAdapter? = null
    private var postList: MutableList<Post>? = null
    private var followingList: MutableList<Post>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyFeedBinding.inflate(inflater, container, false)
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
        binding.feedRecyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        feedAdapter = context?.let { MyFeedAdapter(it, postList as ArrayList<Post>) }
        binding.feedRecyclerView.adapter = feedAdapter

        checkFollowings()
    }

    private fun checkFollowings() {
        followingList = ArrayList()

        val followingRef = FirebaseDatabase.getInstance().reference
            .child("Follow").child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .child("Following")

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    (followingList as ArrayList<String>).clear()

                    for (snapshot in dataSnapshot.children) {
                        snapshot.key?.let { (followingList as ArrayList<String>).add(it) }
                    }
                    retrievePost()

                    if (!(followingList?.size!! > 0)) {
                        binding.progressBar.visibility = View.GONE
                        binding.noResultTextView.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.noResultTextView.visibility = View.GONE
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                context?.toastShort(getString(R.string.txt_something_went_wrong))
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    /**
     * Method shows post of followers only
     * i.e only post of user's friend will be seen
     */
    private fun retrievePost() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList?.clear()

                for (snapshot in dataSnapshot.children) {

                    val post = snapshot.getValue(Post::class.java)

                    for (id in (followingList as ArrayList<String>)) {

                        if (post?.postPublisher == id) {
                            post.screenTitle = getString(R.string.title_feed_detail)
                            postList?.add(post)
                        }
                        feedAdapter?.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }
                }

                if (!(postList?.size!! > 0)) {
                    binding.progressBar.visibility = View.GONE
                    binding.noResultTextView.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.noResultTextView.visibility = View.GONE
                }

            }

            override fun onCancelled(p0: DatabaseError) {
                context?.toastShort(getString(R.string.txt_something_went_wrong))
                binding.progressBar.visibility = View.GONE
            }
        })
    }
}