package com.appforschool.ui.home.fragment.alert

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.ScheduleModel
import com.appforschool.databinding.FragmentAlertBinding
import com.appforschool.databinding.FragmentScheduleBinding
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleViewModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import javax.inject.Inject

class AlertFragment   : BaseBindingFragment<FragmentAlertBinding>() {

    private var listener: HomeListener? = null

    @Inject
    lateinit var viewModel: ScheduleViewModel

    override fun layoutId(): Int = R.layout.fragment_alert

    private var alSchedule: ArrayList<ScheduleModel.Data>? = ArrayList()
    private var binding: FragmentScheduleBinding? = null

    override fun initializeBinding(binding: FragmentAlertBinding) {
        binding.lifecycleOwner = this
    }

    companion object {
        @JvmStatic
        fun newInstance(): ScheduleFragment {
            val homeFragment = ScheduleFragment()
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.scheduleData.observe(viewLifecycleOwner, scheduleData)

        //Make API call
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executeScheduleData()
        } else {
            activity!!.toast(Constant.CHECK_INTERNET)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    private val scheduleData = Observer<ScheduleModel> {
        alSchedule = ArrayList()
        alSchedule!!.addAll(it!!.data!!)
        LogM.e("=> Arraylist size checking " + alSchedule?.size)
        binding?.scheduleList = alSchedule
    }

    interface HomeListener {
        fun popFragment()
    }

    fun closeAlertFragment() {
        listener?.popFragment()
    }

}