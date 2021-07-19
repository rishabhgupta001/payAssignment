package com.sample.vkoelassign.ui.view.adapter

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
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
import com.sample.vkoelassign.ui.view.HomeFragmentDirections
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort

class MovieAdapter2:
    RecyclerView.Adapter<MovieAdapter2.ViewHolder2>() {
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        val itemBinding =
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder2(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        holder.bind(position)
    }

    override fun getItemCount(): Int = 8

    inner class ViewHolder2(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.itemLayout.setOnClickListener {
                itemView.isEnabled = false
                Handler().postDelayed({
                    itemView.isEnabled = true

                    it.context.toastShort("item row clicked")
                    Log.e("vvv", "item clicked")

                    //mPost[adapterPosition].let {
                        //val action = HomeFragmentDirections.actionPostDetail()
                        //action.post = it
                        //Navigation.findNavController(itemBinding.root).navigate(action)
                    //}

                }, 100)
            }

            itemBinding.commentImgView.setOnClickListener {
                it.context.toastShort("commentImgView clicked")
                Log.e("vvv", "commentImgView clicked")
            }
        }

        fun bind(position: Int) {
            val imgUrl = "https://firebasestorage.googleapis.com/v0/b/payassignment-df7db.appspot.com/o/Post%20Pictures%2F1626530367411.jpg?alt=media&token=9ede437e-6800-4714-8c65-efd1fc7a58b0"
            Utils.setImage(itemBinding.postImageHome, imgUrl)
            Utils.setImage(itemBinding.userProfileImageSearch, imgUrl)

           // val data = mPost[position]
             // Utils.setImage(itemBinding.postImageHome, imgUrl.postImage)
           // itemBinding.description.text = data.postDescription
            //setData(itemBinding, data)
        }
    }

    private fun setData(itemBinding: ItemMovieBinding, data: Post) {
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