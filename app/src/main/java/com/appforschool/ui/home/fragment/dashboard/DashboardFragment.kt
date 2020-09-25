package com.appforschool.ui.home.fragment.dashboard

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.databinding.FragmentDashboardBinding
import com.appforschool.databinding.FragmentScheduleBinding
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import com.appforschool.utils.Constant
import com.appforschool.utils.toast

class DashboardFragment : BaseBindingFragment<FragmentDashboardBinding>(){


    override fun layoutId(): Int = R.layout.fragment_dashboard

    override fun initializeBinding(binding: FragmentDashboardBinding) {
        binding.lifecycleOwner = this
        binding.listner = this
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardFragment {
            val dashBoardFragment = DashboardFragment()
            val bundle = Bundle().apply {}
            dashBoardFragment.arguments = bundle
            return dashBoardFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ScheduleFragment.HomeListener) {
//            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
//        viewModel.scheduleData.observe(viewLifecycleOwner, scheduleData)
//
//        //Make API call
//        if (globalMethods.isInternetAvailable(activity!!)) {
//            viewModel.executeScheduleData()
//        } else {
//            activity!!.toast(Constant.CHECK_INTERNET)
//        }
    }

}