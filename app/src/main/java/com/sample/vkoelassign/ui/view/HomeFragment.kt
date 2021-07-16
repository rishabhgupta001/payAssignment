package com.sample.vkoelassign.ui.view

import com.sample.vkoelassign.ui.view.adapter.HomePagerAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.sample.vkoelassign.databinding.FragmentHomeBinding
import com.sample.vkoelassign.ui.viewmodel.LoginViewModel

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: LoginViewModel


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    /**
     * View Initialization
     */
    private fun init() {
        binding.homeViewPager.adapter =
            HomePagerAdapter(requireContext(), requireActivity().supportFragmentManager)
        binding.homeTabLayout.setupWithViewPager(binding.homeViewPager)
    }

}