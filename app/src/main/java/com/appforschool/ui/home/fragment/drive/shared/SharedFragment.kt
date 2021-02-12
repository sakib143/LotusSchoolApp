package com.appforschool.ui.home.fragment.drive.shared

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.databinding.FragmentAnswerBinding
import com.appforschool.databinding.FragmentSharedBinding
import com.appforschool.ui.home.fragment.drive.mydrive.MyDriveFragment

class SharedFragment : BaseBindingFragment<FragmentSharedBinding>() {

    override fun layoutId(): Int = R.layout.fragment_shared

    override fun initializeBinding(binding: FragmentSharedBinding) {
        binding.lifecycleOwner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is DriveFragment.DriveFragmentListner) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
    }

    companion object {
        fun newInstance(): SharedFragment {
            val fragment = SharedFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}