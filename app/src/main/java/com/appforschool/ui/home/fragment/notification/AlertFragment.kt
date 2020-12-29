package com.appforschool.ui.home.fragment.notification

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AlertModel
import com.appforschool.databinding.FragmentAlertBinding
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import kotlinx.android.synthetic.main.fragment_alert.*
import javax.inject.Inject

class AlertFragment: BaseBindingFragment<FragmentAlertBinding>() {

    private var listener: AlertListner? = null

    @Inject
    lateinit var viewModel: AlertViewModel

    override fun layoutId(): Int = R.layout.fragment_alert

    private var alAlert: ArrayList<AlertModel.Data> = ArrayList()
    private var binding: FragmentAlertBinding? = null
    private var adapter: NotificationAdapter? = null

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

        viewModel.setDataFound(true)
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

        adapter = NotificationAdapter( it.data)
        rvNotification.adapter = adapter

//        binding?.alAlert = alAlert
//        if (alAlert?.size == 0) {
//            viewModel.setDataFound(false)
//        } else {
//            viewModel.setDataFound(true)
//        }
    }

    interface AlertListner {
        fun popFragment()
    }

    fun closeAlertFragment() {
        listener?.popFragment()
    }

}