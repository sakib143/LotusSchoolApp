package com.appforschool.ui.home.fragment.drive

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.DriveModel
import com.appforschool.databinding.FragmentAssignmentBinding
import com.appforschool.databinding.FragmentDriveBinding
import com.appforschool.ui.home.fragment.assignment.AssignmentFragment
import com.appforschool.ui.home.fragment.assignment.AssignmentViewModel
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import javax.inject.Inject

class DriveFragment: BaseBindingFragment<FragmentDriveBinding>() {

    private var listener: DriveFragmentListner? = null

    @Inject
    lateinit var viewModel: DriveViewModel

    override fun layoutId(): Int = R.layout.fragment_drive

    private var alDriveData: ArrayList<DriveModel.Data>? = ArrayList()
    private var binding: FragmentDriveBinding? = null


    override fun initializeBinding(binding: FragmentDriveBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
        this.binding = binding
        binding.driveList = alDriveData
    }

    companion object {
        fun newInstance(): DriveFragment {
            val fragment = DriveFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DriveFragmentListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.setDataFound(true)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.driveList.observe(viewLifecycleOwner, driveListObserver)
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executerDriveList()
        } else {
            activity!!.toast(activity!!.resources.getString(R.string.check_internet_connection))
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    private val driveListObserver = Observer<DriveModel> {
        alDriveData = ArrayList()
        alDriveData!!.addAll(it.data)
        binding?.driveList = alDriveData
        if (alDriveData?.size == 0) {
            viewModel.setDataFound(false)
        } else {
            viewModel.setDataFound(true)
        }
    }

    interface DriveFragmentListner {
        fun popFragment()
    }

    fun closeDriveFragment() {
        listener?.popFragment()
    }
}