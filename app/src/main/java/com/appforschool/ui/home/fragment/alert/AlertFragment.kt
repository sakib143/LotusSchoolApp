package com.appforschool.ui.home.fragment.alert

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.ScheduleModel
import com.appforschool.databinding.FragmentAlertBinding
import com.appforschool.databinding.FragmentScheduleBinding
import com.appforschool.ui.home.fragment.assignment.AssignmentFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleViewModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import javax.inject.Inject

class AlertFragment: BaseBindingFragment<FragmentAlertBinding>() {

    private var listener: AlertListner? = null

    @Inject
    lateinit var viewModel: AlertViewModel

    override fun layoutId(): Int = R.layout.fragment_alert

    private var alAlert: ArrayList<AlertModel.Data> = ArrayList()
    private var binding: FragmentAlertBinding? = null

    override fun initializeBinding(binding: FragmentAlertBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
        this.binding = binding
        binding.alAlert = alAlert
    }

    companion object {
        fun newInstance(): AlertFragment {
            val fragment = AlertFragment()
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AlertListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.alertData.observe(viewLifecycleOwner, alertObserver)

        //Make API call
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executeAlert()
        } else {
            activity!!.toast(Constant.CHECK_INTERNET)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    private val alertObserver = Observer<AlertModel> {
        alAlert = ArrayList()
        alAlert!!.addAll(it!!.data!!)
        LogM.e("=> Arraylist size checking " + alAlert?.size)
        binding?.alAlert = alAlert
    }

    interface AlertListner {
        fun popFragment()
    }

    fun closeAlertFragment() {
        listener?.popFragment()
    }

}