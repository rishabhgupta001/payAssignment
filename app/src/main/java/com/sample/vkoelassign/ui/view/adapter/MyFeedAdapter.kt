package com.sample.vkoelassign.ui.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sample.vkoelassign.utility.toastShort

class MyFeedAdapter(
    private var mContext: Context,
    private var mPost: List<Post>
) : RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder>() {
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFeedViewHolder {
       /* val itemBinding =
            ItemFeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFeedViewHolder(itemBinding)*/

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_feed_layout, parent, false)
        return MyFeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyFeedViewHolder, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        //holder.bind(position)

        holder.itemView.setOnClickListener {
            Log.e("vvv", "itemCard clicked")
            it.context.toastShort("itemCard clicked")
        }
    }

    override fun getItemCount(): Int = 10

    inner class MyFeedViewHolder(private var itemBinding: View) :
        RecyclerView.ViewHolder(itemBinding) {

/*        init {
            itemBinding.setOnClickListener {
                *//*itemView.isEnabled = false
                Handler().postDelayed({
                    itemView.isEnabled = true*//*

                Log.e("vvv", "itemCard clicked")
                it.context.toastShort("itemCard clicked")

                *//*mPost[adapterPosition].let {
                    val action = HomeFragmentDirections.actionPostDetail()
                    //action.post = it
                    Navigation.findNavController(itemBinding.root).navigate(action)
                }*//*

                //}, 100)
            }


          *//*  itemBinding.commentImgView.setOnClickListener {
                it.context.toastShort("postImageCommentBtn clicked")
                *//**//*itemView.isEnabled = false
                Handler().postDelayed({
                    itemView.isEnabled = true*//**//*

                *//**//*mPost[adapterPosition].let {
                    val action = HomeFragmentDirections.actionPostDetail()
                    //action.post = it
                    Navigation.findNavController(itemBinding.root).navigate(action)
                }*//**//*

                //}, 100)
            }*//*
        }*/

        fun bind(position: Int) {
            val data = mPost[position]
            //Utils.setImage(itemBinding.postImageHome, data.postImage)
            //setData(itemBinding, data)
        }
    }

    private fun setData(itemBinding: ItemFeedLayoutBinding, data: Post) {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
            .child(data.postPublisher)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    /*val user = p0.getValue<User>(User::class.java)
                    itemBinding.userNameSearch.text = user?.userName
                    itemBinding.publisher.text = user?.fullName
                    Utils.setImage(itemBinding.userProfileImageSearch, user?.image!!)*/
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}