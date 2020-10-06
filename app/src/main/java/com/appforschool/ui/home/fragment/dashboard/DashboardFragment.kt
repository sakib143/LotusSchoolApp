package com.appforschool.ui.home.fragment.dashboard

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.databinding.FragmentDashboardBinding
import com.appforschool.ui.home.fragment.schedule.ScheduleViewModel
import com.appforschool.utils.LogM
import javax.inject.Inject

class DashboardFragment : BaseBindingFragment<FragmentDashboardBinding>() {

    private var listener: FragmentListner? = null

    override fun layoutId(): Int = R.layout.fragment_dashboard

    @Inject
    lateinit var viewModel: DashboardviewModel

    override fun initializeBinding(binding: FragmentDashboardBinding) {
        binding.lifecycleOwner = this
        binding.listner = this
        binding.viewmodel = viewModel
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
        viewModel.getUserName()
    }

    interface FragmentListner {
        fun popFragment()
        fun openScheduleFragment()
        fun openHamBurgerMenu()
        fun openSubjectFragment()
        fun openAssignmentFragment()
        fun openAlertFragment()
        fun openExamListFragment()
        fun openDriveFragment()
    }

    fun openScheduleFragment() {
        listener?.openScheduleFragment()
    }

    fun openHamBurgerMenu() {
        listener?.openHamBurgerMenu()
    }

    fun openSubjectFragment() {
        listener?.openSubjectFragment()
    }

    fun openAssignment() {
        listener?.openAssignmentFragment()
    }

    fun openAlert() {
        listener?.openAlertFragment()
    }

    fun openExamFragment() {
        listener?.openExamListFragment()
    }

    fun openDriveFragment() {
        listener?.openDriveFragment()
    }

}