package com.appforschool.ui.home.fragment.drive.shared

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.DriveModel
import com.appforschool.databinding.FragmentAnswerBinding
import com.appforschool.databinding.FragmentSharedBinding
import com.appforschool.ui.home.fragment.drive.mydrive.MyDriveFragment
import com.appforschool.ui.home.fragment.drive.mydrive.MyDriveViewModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import javax.inject.Inject

class SharedFragment : BaseBindingFragment<FragmentSharedBinding>() {

    @Inject
    lateinit var viewModel: ShareDriveViewModel
    private var alShared: ArrayList<DriveModel.Data>? = ArrayList()
    private var binding: FragmentSharedBinding? = null

    override fun layoutId(): Int = R.layout.fragment_shared

    override fun initializeBinding(binding: FragmentSharedBinding) {
        binding.lifecycleOwner = this
        this.binding = binding
        binding.driveList = alShared
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
        fun newInstance(): SharedFragment {
            val fragment = SharedFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = this.arguments
        alShared?.addAll(bundle?.getParcelableArrayList(Constant.KEY_DRIVE_DATA)!!)
        if(alShared?.size == 0) {
            viewModel.setNoDataFound(false)
        } else {
            viewModel.setNoDataFound(true)
        }
    }
}