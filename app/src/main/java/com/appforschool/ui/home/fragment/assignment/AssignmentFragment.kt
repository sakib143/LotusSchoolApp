package com.appforschool.ui.home.fragment.assignment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AssignmentModel
import com.appforschool.databinding.FragmentAssignmentBinding
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import javax.inject.Inject

class AssignmentFragment : BaseBindingFragment<FragmentAssignmentBinding>() {

    private var listener: AssignmentFragmentListner? = null

    @Inject
    lateinit var viewModel: AssignmentViewModel

    override fun layoutId(): Int = R.layout.fragment_assignment

    private var alAssignment: ArrayList<AssignmentModel.Data>? = ArrayList()
    private var binding: FragmentAssignmentBinding? = null

    override fun initializeBinding(binding: FragmentAssignmentBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
        binding.assignmentList = alAssignment
        this.binding = binding
    }

    companion object {
        fun newInstance(): AssignmentFragment {
            val assignmentFragment = AssignmentFragment()
            return assignmentFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AssignmentFragmentListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setObserver()
    }

    private fun setObserver() {
        viewModel.setDataFound(true)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.subjectDetails.observe(viewLifecycleOwner, assignmentObserver)
        executeAPI()
    }

    fun executeAPI() {
        if (globalMethods.isInternetAvailable(requireActivity())) {
            viewModel.executerAssigment()
        } else {
            requireActivity().toast(requireActivity().resources.getString(R.string.check_internet_connection))
        }
    }

    private val assignmentObserver = Observer<AssignmentModel> {
        alAssignment = ArrayList()
        alAssignment!!.addAll(it.data)
        binding?.assignmentList = alAssignment
        if (alAssignment?.size == 0) {
            viewModel.setDataFound(false)
        } else {
            viewModel.setDataFound(true)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    interface AssignmentFragmentListner {
        fun popFragment()
    }

    fun closeAssignmnent() {
        listener?.popFragment()
    }
}