package com.sample.vkoelassign.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.ItemFeedLayoutBinding
import com.sample.vkoelassign.network.Post
import com.sample.vkoelassign.network.User
import com.sample.vkoelassign.utility.Utils
import de.hdodenhof.circleimageview.CircleImageView

class MyFeedAdapter(
    private var mContext: Context,
    private var mPost: List<Post>
) : RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder>() {
    private lateinit var binding: ItemFeedLayoutBinding
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFeedViewHolder {
        val itemBinding =
            ItemFeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyFeedViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        holder.bind(position)
    }

    override fun getItemCount(): Int = mPost.size

    inner class MyFeedViewHolder(private var itemBinding: ItemFeedLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(position: Int) {
            val data = mPost[position]
            Utils.setImage(itemBinding.postImageHome, data.postImage)
            setData(itemBinding, data)
        }
    }

    private fun setData(itemBinding: ItemFeedLayoutBinding, data: Post) {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
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

}