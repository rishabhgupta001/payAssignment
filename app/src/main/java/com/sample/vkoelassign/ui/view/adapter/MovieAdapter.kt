package com.sample.vkoelassign.ui.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.databinding.ItemMovieBinding
import com.sample.vkoelassign.network.Post
import com.sample.vkoelassign.network.User
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort

class MovieAdapter(
    private var mContext: Context,
    private var mPost: List<Post>
) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private var firebaseUser: FirebaseUser? = null
    private var mPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        mPosition = position
        holder.bind(position)
    }

    override fun getItemCount(): Int = mPost.size

    inner class ViewHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.itemLayout.setOnClickListener {
                it.context.toastShort("on row ${mPosition} clicked")
            }
        }

        fun bind(position: Int) {
            val data = mPost[position]
            Utils.setImage(itemBinding.postImageHome, data.postImage)
            itemBinding.description.text = data.postDescription
            setData(itemBinding, data)
            Log.e("MovieAdapter", "postId:- ${data.postId}   publisherId:- ${data.postPublisher}")


            //addComments(data)
            getCommentsCount(itemBinding, data)
        }
    }

    private fun setData(itemBinding: ItemMovieBinding, data: Post) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child(data.postPublisher)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)
                    itemBinding.userNameSearch.text = user?.userName
                    itemBinding.publisher.text = user?.fullName
                    Utils.setImage(itemBinding.userProfileImageSearch, user?.image!!)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun addComments(data: Post) {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(data.postId)

        val commentsMap = HashMap<String, Any>()
        commentsMap["comment"] = "Awesome"
        commentsMap["publisher"] = FirebaseAuth.getInstance().currentUser?.uid!!
        commentsRef.push().setValue(commentsMap)
    }

    private fun getCommentsCount(itemBinding: ItemMovieBinding, data: Post) {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(data.postId)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val commentsCount:String = "view all ${dataSnapshot.childrenCount} comments"
                    itemBinding.comments.setText(commentsCount)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}