package com.appforschool.ui.home.fragment.subject

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.SubjectModel
import com.appforschool.databinding.FragmentSubjectsBinding
import com.appforschool.ui.home.fragment.schedule.ScheduleViewModel
import com.appforschool.utils.toast
import kotlinx.android.synthetic.main.fragment_subjects.*
import javax.inject.Inject

class SubjectFragment : BaseBindingFragment<FragmentSubjectsBinding>() {

    private var adapter: SubjectAdapter? = null
    private var listener: SubjectFragmentListner? = null

    override fun layoutId(): Int = R.layout.fragment_subjects

    @Inject
    lateinit var viewModel: SubjectViewModel

    override fun initializeBinding(binding: FragmentSubjectsBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
    }

    companion object {
        @JvmStatic
        fun newInstance(): SubjectFragment {
            val fragment = SubjectFragment()
            val bundle = Bundle().apply {}
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SubjectFragmentListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.subjectData.observe(viewLifecycleOwner, subjectObserver)
        viewModel.executerSubject()
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    private val subjectObserver = Observer<SubjectModel> {
        if (it.status) {
            rvSubjectFragment.setHasFixedSize(true)
            rvSubjectFragment.layoutManager = GridLayoutManager(activity!!.applicationContext, 2)
            adapter = SubjectAdapter(it.data, activity!!, object : SubjectFragmentListner {
                override fun popFragment() {

                }

                override fun openVideo(url: String) {

                }
            })
            rvSubjectFragment.adapter = adapter
        } else {
            activity?.toast(it!!.message)
        }
    }

    interface SubjectFragmentListner {
        fun popFragment()
        fun openVideo(url: String)
    }

    fun closeSubjectFragment() {
        listener?.popFragment()
    }
}