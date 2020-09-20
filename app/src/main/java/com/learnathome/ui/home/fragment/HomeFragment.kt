package com.learnathome.ui.home.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.learnathome.R
import com.learnathome.base.BaseBindingFragment
import com.learnathome.databinding.FragmentHomeBinding
import com.learnathome.utils.Constant
import com.learnathome.utils.LogM
import com.learnathome.utils.toast
import java.net.URLEncoder
import javax.inject.Inject

class HomeFragment  : BaseBindingFragment<FragmentHomeBinding>() {

    private var listener: HomeListener? = null

    @Inject
    lateinit var viewModel: HomeFragmentViewModel

    override fun layoutId(): Int = R.layout.fragment_home

    override fun initializeBinding(binding: FragmentHomeBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment {
            val homeFragment = HomeFragment()
            val bundle = Bundle().apply {}
            homeFragment.arguments = bundle
            return homeFragment
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    interface HomeListener {
        fun popFragment()
        fun openHamBurgerMenu()
    }

    fun openHamBurgerMenu() {
        listener?.openHamBurgerMenu()
    }
}