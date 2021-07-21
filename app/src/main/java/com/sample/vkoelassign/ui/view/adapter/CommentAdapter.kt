package com.sample.vkoelassign.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.databinding.ItemCommentBinding
import com.sample.vkoelassign.data.network.Comment
import com.sample.vkoelassign.data.network.User
import com.sample.vkoelassign.utility.Utils

class CommentAdapter(
    private var mContext: Context,
    private var mComment: List<Comment>
) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = mComment.size

    inner class ViewHolder(private val itemBinding: ItemCommentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(position: Int) {
            val data = mComment[position]
            //itemBinding.commentTextView.text = data.comment
            Utils.showBounceAnimOnText(mContext, itemBinding.commentTextView, data.comment)
            setData(itemBinding, data)
        }
    }

    private fun setData(itemBinding: ItemCommentBinding, data: Comment) {
        val usersRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child(data.publisher)

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user = p0.getValue<User>(User::class.java)
                    itemBinding.profileNameTextView.text = user?.userName
                    Utils.setImage(itemBinding.userProfileImageSearch, user?.image!!)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}