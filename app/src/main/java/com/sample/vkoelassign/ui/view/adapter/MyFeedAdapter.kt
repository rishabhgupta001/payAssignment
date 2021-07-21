package com.sample.vkoelassign.ui.view.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.databinding.ItemFeedLayoutBinding
import com.sample.vkoelassign.data.network.Post
import com.sample.vkoelassign.data.network.User
import com.sample.vkoelassign.ui.view.HomeFragmentDirections
import com.sample.vkoelassign.utility.Utils

class MyFeedAdapter(
    private var mContext: Context,
    private var mPost: List<Post>
) : RecyclerView.Adapter<MyFeedAdapter.MyFeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFeedViewHolder {
        val itemBinding =
            ItemFeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyFeedViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyFeedViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = mPost.size

    inner class MyFeedViewHolder(private var itemBinding: ItemFeedLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                itemView.isEnabled = false
                Handler().postDelayed({
                    itemView.isEnabled = true

                    mPost[adapterPosition].let {
                        val action = HomeFragmentDirections.actionPostDetail()
                        action.post = it
                        Navigation.findNavController(itemBinding.root).navigate(action)
                    }
                }, 100)
            }

            itemBinding.commentImgView.setOnClickListener {
                itemBinding.commentImgView.isEnabled = false
                Handler().postDelayed({
                    itemBinding.commentImgView.isEnabled = true

                    mPost[adapterPosition].let {
                        val action = HomeFragmentDirections.actionComment()
                        action.post = it
                        Navigation.findNavController(itemBinding.root).navigate(action)
                    }
                }, 100)
            }
        }

        fun bind(position: Int) {
            val data = mPost[position]

            Utils.showFadeInAnimOnText(mContext, itemBinding.description, data.postDescription)
            //itemBinding.description.text = data.postDescription
            Utils.setImage(itemBinding.postImageHome, data.postImage)
            setData(itemBinding, data)
            getCommentsCount(itemBinding, data)
        }
    }

    private fun setData(itemBinding: ItemFeedLayoutBinding, data: Post) {
        val usersRef = FirebaseDatabase.getInstance().getReference().child("Users")
            .child(data.postPublisher)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)

                    Utils.showSlideDownAnimOnText(mContext, itemBinding.userNameSearch, user?.userName!!)
                    //itemBinding.userNameSearch.text = user?.userName
                    Utils.showSlideDownAnimOnText(mContext, itemBinding.publisher, user.fullName)
                    //itemBinding.publisher.text = user?.fullName
                    Utils.setImage(itemBinding.userProfileImageSearch, user.image)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun getCommentsCount(itemBinding: ItemFeedLayoutBinding, data: Post) {
        val commentsRef = FirebaseDatabase.getInstance().reference
            .child("Comments")
            .child(data.postId)

        commentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var commentsCount = ""
                    if (dataSnapshot.childrenCount.toInt() == 1) {
                        commentsCount = "${dataSnapshot.childrenCount} comment"
                    }
                    if (dataSnapshot.childrenCount.toInt() >= 1) {
                        commentsCount = "${dataSnapshot.childrenCount} comments"
                    }
                    if (dataSnapshot.childrenCount.toInt() == 0) {
                        commentsCount = ""

                    }
                    //itemBinding.comments.setText(commentsCount)
                    Utils.showBounceAnimOnText(mContext, itemBinding.comments, commentsCount)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}