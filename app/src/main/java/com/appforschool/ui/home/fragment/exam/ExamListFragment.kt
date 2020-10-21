package com.appforschool.ui.home.fragment.exam

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.ExamModel
import com.appforschool.databinding.FragmentAlertBinding
import com.appforschool.databinding.FragmentExamlistBinding
import com.appforschool.ui.home.fragment.alert.AlertFragment
import com.appforschool.ui.home.fragment.alert.AlertViewModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import javax.inject.Inject

class ExamListFragment : BaseBindingFragment<FragmentExamlistBinding>() {

    private var listener: ExamListListner? = null

    @Inject
    lateinit var viewModel: ExamViewModel

    override fun layoutId(): Int = R.layout.fragment_examlist

    private var alExamp: ArrayList<ExamModel.Data> = ArrayList()
    private var binding: FragmentExamlistBinding? = null

    override fun initializeBinding(binding: FragmentExamlistBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
        this.binding = binding
        binding.examList = alExamp
    }

    companion object {
        fun newInstance(): ExamListFragment {
            val fragment = ExamListFragment()
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ExamListListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.setDataFound(true)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.examData.observe(viewLifecycleOwner, examListObserver)

        //Make API call
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executeExamData()
        } else {
            activity!!.toast(Constant.CHECK_INTERNET)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    private val examListObserver = Observer<ExamModel> {
        alExamp = ArrayList()
        alExamp!!.addAll(it!!.data!!)
        binding?.examList = alExamp
        if (alExamp?.size == 0) {
            viewModel.setDataFound(false)
        } else {
            viewModel.setDataFound(true)
        }
    }

    interface ExamListListner {
        fun popFragment()
    }

    fun closeAlertFragment() {
        listener?.popFragment()
    }

}