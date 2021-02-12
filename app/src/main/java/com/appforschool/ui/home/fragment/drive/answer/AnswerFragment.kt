package com.appforschool.ui.home.fragment.drive.answer

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.databinding.FragmentAnswerBinding
import com.appforschool.databinding.FragmentDriveBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.home.fragment.drive.DriveFragment

class AnswerFragment : BaseBindingFragment<FragmentAnswerBinding>() {

    override fun layoutId(): Int = R.layout.fragment_answer

    override fun initializeBinding(binding: FragmentAnswerBinding) {
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
        fun newInstance(): AnswerFragment {
            val fragment = AnswerFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}