package com.sample.vkoelassign.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.vkoelassign.databinding.FragmentMyFeedBinding

class MyFeedFragment : Fragment() {
    private lateinit var binding: FragmentMyFeedBinding
    //private lateinit var scrollAdapter: ScrollRecyclerAdapter

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
        /*scrollAdapter = ScrollRecyclerAdapter(requireContext(), listData, activity?.supportFragmentManager!!)
        binding.scrollRecyclerView.adapter = scrollAdapter*/

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.recyclerView.layoutManager = linearLayoutManager
    }
}