package com.appforschool.ui.home.fragment.drive.mydrive

import android.content.Context
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.DriveModel
import com.appforschool.databinding.FragmentAnswerBinding
import com.appforschool.databinding.FragmentMyDriveBinding
import com.appforschool.ui.home.fragment.drive.DriveFragment
import com.appforschool.ui.home.fragment.drive.DriveViewModel
import com.appforschool.ui.home.fragment.drive.answer.AnswerFragment
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import javax.inject.Inject

class MyDriveFragment : BaseBindingFragment<FragmentMyDriveBinding>()  {

    private var alDrive: ArrayList<DriveModel.Data>? = ArrayList()

    override fun layoutId(): Int = R.layout.fragment_my_drive

    @Inject
    lateinit var viewModel: MyDriveViewModel

    override fun initializeBinding(binding: FragmentMyDriveBinding) {
        binding.lifecycleOwner = this
        binding.driveList = alDrive
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
        fun newInstance(): MyDriveFragment {
            val fragment = MyDriveFragment()
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = this.arguments
        alDrive?.addAll(bundle?.getParcelableArrayList(Constant.KEY_DRIVE_DATA)!!)
        LogM.e("Arraylist size is " + alDrive?.size)

        if(alDrive?.size == 0) {

        } else {

        }

    }

}