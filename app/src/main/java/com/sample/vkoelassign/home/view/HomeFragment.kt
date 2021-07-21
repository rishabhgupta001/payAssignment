package com.sample.vkoelassign.home.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sample.vkoelassign.R
import com.sample.vkoelassign.databinding.FragmentHomeBinding
import com.sample.vkoelassign.home.view.adapter.HomePagerAdapter
import com.sample.vkoelassign.onboarding.viewmodel.LoginViewModel
import com.sample.vkoelassign.utility.Utils
import com.sample.vkoelassign.utility.toastShort


/**
 *
 * Purpose – This screen contains 2 tabs:- MyFeed and MyPost
 *
 * @author Rishabh Gupta
 *
 * */
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: LoginViewModel

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
    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        if (Utils.isInternetAvailable(context)) {
            binding.homeViewPager.adapter =
                HomePagerAdapter(requireContext(), childFragmentManager)
            binding.homeTabLayout.setupWithViewPager(binding.homeViewPager)
        } else {
            context?.toastShort(getString(R.string.text_make_sure_no_data_connection))
        }
    }

    override fun onResume() {
        super.onResume()
        init()
    }

}