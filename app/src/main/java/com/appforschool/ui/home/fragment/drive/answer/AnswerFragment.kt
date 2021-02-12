package com.appforschool.ui.home.fragment.drive.answer

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.DriveModel
import com.appforschool.databinding.FragmentAnswerBinding
import com.appforschool.databinding.FragmentDriveBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.home.fragment.drive.DriveFragment
import com.appforschool.ui.home.fragment.drive.mydrive.MyDriveViewModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import javax.inject.Inject

class AnswerFragment : BaseBindingFragment<FragmentAnswerBinding>() {

    private var alAnswer: ArrayList<DriveModel.Data>? = ArrayList()



    @Inject
    lateinit var viewModel: AnswerViewModel
    private var binding: FragmentAnswerBinding? = null

    override fun layoutId(): Int = R.layout.fragment_answer

    override fun initializeBinding(binding: FragmentAnswerBinding) {
        binding.lifecycleOwner = this
        this.binding = binding
        binding.driveList = alAnswer
        binding.viewmodel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is DriveFragment.DriveFragmentListner) {
//            listener = context
//        } else {
//            throw RuntimeException("$context must implement OnFragmentInteractionListener")
//        }
    }

    companion object {
        fun newInstance(): AnswerFragment {
            val fragment = AnswerFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = this.arguments
        alAnswer?.addAll(bundle?.getParcelableArrayList(Constant.KEY_DRIVE_DATA)!!)
        if(alAnswer?.size == 0) {
            viewModel.setNoDataFound(false)
        } else {
            viewModel.setNoDataFound(true)
        }
    }

}