package com.appforschool.ui.home.fragment.assignment

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.databinding.FragmentAssignmentBinding
import com.appforschool.databinding.FragmentSubjectDetailsBinding
import com.appforschool.ui.home.fragment.subject.subjectdetails.SubjectDetailsFragment
import com.appforschool.ui.home.fragment.subject.subjectdetails.SubjectDetailsViewModel
import com.appforschool.utils.Constant
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
        this.binding = binding
        binding.assignmentList = alAssignment
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

        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.subjectDetails.observe(viewLifecycleOwner, assignmentObserver)
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executerAssigment()
        } else {
            activity!!.toast(activity!!.resources.getString(R.string.check_internet_connection))
        }
    }

    private val assignmentObserver = Observer<AssignmentModel> {
        alAssignment = ArrayList()
        alAssignment!!.addAll(it.data)
        LogM.e("=> Arraylist size checking " + alAssignment?.size)
        binding?.assignmentList = alAssignment
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