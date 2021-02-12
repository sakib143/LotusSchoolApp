package com.appforschool.ui.home.fragment.drive.mydrive

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.databinding.FragmentAnswerBinding
import com.appforschool.databinding.FragmentMyDriveBinding
import com.appforschool.ui.home.fragment.drive.DriveFragment
import com.appforschool.ui.home.fragment.drive.answer.AnswerFragment

class MyDriveFragment : BaseBindingFragment<FragmentMyDriveBinding>()  {

    override fun layoutId(): Int = R.layout.fragment_my_drive

    override fun initializeBinding(binding: FragmentMyDriveBinding) {
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
        fun newInstance(): MyDriveFragment {
            val fragment = MyDriveFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}