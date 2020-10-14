package com.appforschool.ui.home.fragment.subject.subjectdetails

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.data.model.SubjectModel
import com.appforschool.databinding.FragmentScheduleBinding
import com.appforschool.databinding.FragmentSubjectDetailsBinding
import com.appforschool.databinding.FragmentSubjectsBinding
import com.appforschool.ui.home.fragment.schedule.ScheduleViewModel
import com.appforschool.ui.home.fragment.subject.SubjectAdapter
import com.appforschool.ui.home.fragment.subject.SubjectFragment
import com.appforschool.ui.home.fragment.subject.SubjectViewModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import kotlinx.android.synthetic.main.fragment_subjects.*
import javax.inject.Inject

class SubjectDetailsFragment  : BaseBindingFragment<FragmentSubjectDetailsBinding>() {

    private var listener: SubjectDetailsListner? = null

    @Inject
    lateinit var viewModel: SubjectDetailsViewModel

    override fun layoutId(): Int = R.layout.fragment_subject_details

    private var alSubjectDetails: ArrayList<SubjectDetailsModel.Data>? = ArrayList()
    private var binding: FragmentSubjectDetailsBinding? = null
    private var subjectId : String? = null
    private var subjectName: String?= null


    override fun initializeBinding(binding: FragmentSubjectDetailsBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
        this.binding = binding
        binding.subjectDetailsList = alSubjectDetails
    }

    companion object {
        fun newInstance(subjectId: String, subjectName: String): SubjectDetailsFragment {
            val productFragment = SubjectDetailsFragment()
            val bundle = Bundle().apply {
                putString(Constant.REUQEST_SUBJECT_ID, subjectId)
                putString(Constant.REUQEST_GET_SUBJECTS, subjectName)
            }
            productFragment.arguments = bundle
            return productFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectId = it.getString(Constant.REUQEST_SUBJECT_ID)
            subjectName = it.getString(Constant.REUQEST_GET_SUBJECTS)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SubjectDetailsListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.subjectDetails.observe(viewLifecycleOwner, subjectDetails)
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executerDetails(subjectId!!)
        }else {
            activity!!.toast(activity!!.resources.getString(R.string.check_internet_connection))
        }

        viewModel.setSubjectName(subjectName!!)
    }

    private val subjectDetails = Observer<SubjectDetailsModel> {
        alSubjectDetails = ArrayList()
        alSubjectDetails!!.addAll(it.data)
        LogM.e("=> Arraylist size checking " + alSubjectDetails?.size)
        binding?.subjectDetailsList = alSubjectDetails
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    interface SubjectDetailsListner {
        fun popFragment()
    }

    fun closeSubjectDetails() {
        listener?.popFragment()
    }
}