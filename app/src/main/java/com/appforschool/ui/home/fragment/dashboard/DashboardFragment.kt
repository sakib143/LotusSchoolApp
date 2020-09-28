package com.appforschool.ui.home.fragment.dashboard

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.databinding.FragmentDashboardBinding

class DashboardFragment : BaseBindingFragment<FragmentDashboardBinding>() {

    private var listener: FragmentListner? = null

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
        if (context is FragmentListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    interface FragmentListner {
        fun popFragment()
        fun openScheduleFragment()
        fun openHamBurgerMenu()
    }

    fun openScheduleFragment() {
        listener?.openScheduleFragment()
    }

    fun openHamBurgerMenu() {
        listener?.openHamBurgerMenu()
    }

}