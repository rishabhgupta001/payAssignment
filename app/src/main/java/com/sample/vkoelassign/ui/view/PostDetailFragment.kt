package com.sample.vkoelassign.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sample.vkoelassign.databinding.FragmentPostDetailBinding
import com.sample.vkoelassign.network.Post
import com.sample.vkoelassign.utility.Utils

class PostDetailFragment : Fragment() {
    private lateinit var binding: FragmentPostDetailBinding
    private var itemData: Post? = null

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
            itemData = PostDetailFragmentArgs.fromBundle(it).post
            Utils.setImage(binding.detailImgView, itemData?.postImage!!)
        }
    }

}