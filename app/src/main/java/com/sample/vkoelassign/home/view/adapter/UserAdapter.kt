package com.sample.vkoelassign.home.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sample.vkoelassign.R
import com.sample.vkoelassign.data.network.User
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort
import de.hdodenhof.circleimageview.CircleImageView


/**
 *
 * Purpose – Adapter for Search Screen
 *
 * @author Rishabh Gupta
 *
 * */
class UserAdapter(
    private var mContext: Context,
    private var mUser: List<User>, private var isFragment: Boolean = false
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameTextView: TextView = itemView.findViewById(R.id.nav_username_text_view)
        var userFullnameTextView: TextView = itemView.findViewById(R.id.nav_user_profile_name)
        var userMobileNum: TextView = itemView.findViewById(R.id.nav_user_phone_num_text_view)
        var userProfileImage: CircleImageView =
            itemView.findViewById(R.id.profile_image_view)
        var followButton: Button = itemView.findViewById(R.id.follow_btn)

        fun bind(position: Int) {
            val user = mUser[position]

            userNameTextView.text = user.userName
            userFullnameTextView.text = user.fullName
            userMobileNum.text = user.mobileNum
            Utils.setImage(userProfileImage, user.image)

            checkFollowingStatus(user.uid, followButton)

            itemView.setOnClickListener(View.OnClickListener {
                it.context.toastShort("Clicked On Item:-  Open Profile Fragment")
            })

            followButton.setOnClickListener {
                if (followButton.text.toString() == "Follow") {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(user.uid)
                            .setValue(true).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firebaseUser?.uid.let { it ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow").child(user.uid)
                                            .child("Followers").child(it.toString())
                                            .setValue(true).addOnCompleteListener { task ->
                                                if (task.isSuccessful) {

                                                }
                                            }
                                    }
                                }
                            }
                    }
                } else {
                    firebaseUser?.uid.let { it1 ->
                        FirebaseDatabase.getInstance().reference
                            .child("Follow").child(it1.toString())
                            .child("Following").child(user.uid)
                            .removeValue().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firebaseUser?.uid.let { it1 ->
                                        FirebaseDatabase.getInstance().reference
                                            .child("Follow").child(user.uid)
                                            .child("Followers").child(it1.toString())
                                            .removeValue().addOnCompleteListener { task ->
                                                if (task.isSuccessful) {

                                                }
                                            }
                                    }
                                }
                            }
                    }
                }
            }

        }
    }


    private fun checkFollowingStatus(uid: String, followButton: Button) {
        val followingRef = firebaseUser?.uid.let { it1 ->
            FirebaseDatabase.getInstance().reference
                .child("Follow").child(it1.toString())
                .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.child(uid).exists()) {
                    followButton.text = "Following"
                } else {
                    followButton.text = "Follow"
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}