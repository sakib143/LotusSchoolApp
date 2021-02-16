package com.appforschool.ui.home.fragment.drive

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.DriveModel
import com.appforschool.databinding.FragmentDriveBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.home.fragment.drive.answer.AnswerFragment
import com.appforschool.ui.home.fragment.drive.mydrive.MyDriveFragment
import com.appforschool.ui.home.fragment.drive.shared.SharedFragment
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_drive.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class DriveFragment : BaseBindingFragment<FragmentDriveBinding>(),
    UserProfileListner.onScreenCloseListner {

    private var listener: DriveFragmentListner? = null

    @Inject
    lateinit var viewModel: DriveViewModel

    override fun layoutId(): Int = R.layout.fragment_drive

    private var alMainList: ArrayList<DriveModel.Data>? = ArrayList()
    private var alDrive: ArrayList<DriveModel.Data>? = ArrayList()
    private var alShared: ArrayList<DriveModel.Data>? = ArrayList()
    private var alAnswer: ArrayList<DriveModel.Data>? = ArrayList()
    private var binding: FragmentDriveBinding? = null

    private var myDriveFragment: MyDriveFragment? = null
    private var sharedfragment: SharedFragment? = null
    private var answerfragment: AnswerFragment? = null


    override fun initializeBinding(binding: FragmentDriveBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
        this.binding = binding
        binding.driveList = alMainList
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

        UserProfileListner.getInstance().setListener(this)
        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.driveList.observe(viewLifecycleOwner, driveListObserver)
        getDriveList()
    }

    private fun setViewPagerStuff() {
        //Set My drive
        myDriveFragment = MyDriveFragment.newInstance()
        val bundleMyDrive = Bundle()
        bundleMyDrive.putParcelableArrayList(Constant.KEY_DRIVE_DATA, alDrive)
        myDriveFragment?.arguments = bundleMyDrive

        //Set Share
        sharedfragment = SharedFragment.newInstance()
        val bundleShare = Bundle()
        bundleShare.putParcelableArrayList(Constant.KEY_DRIVE_DATA, alShared)
        sharedfragment?.arguments = bundleShare

        //ANSWER
        answerfragment = AnswerFragment.newInstance()
        val bundleAnswer = Bundle()
        bundleAnswer.putParcelableArrayList(Constant.KEY_DRIVE_DATA, alAnswer)
        answerfragment?.arguments = bundleAnswer


        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> myDriveFragment!!
                    1 -> sharedfragment!!
                    2 -> answerfragment!!
                    else -> myDriveFragment!!
                }
            }

            override fun getItemCount(): Int {
                return 3
            }
        }

        //Tab Cap Inj Syrup Oint Pwdr Spray Drops E&E Pine
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.drive)
                1 -> resources.getString(R.string.shared)
                2 -> resources.getString(R.string.answer)
                else -> resources.getString(R.string.drive)
            }
        }.attach()

        tabs.getTabAt(1)?.setIcon(R.drawable.ic_share_tab)

    }

    fun getDriveList() {
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
        alMainList = ArrayList()
        alMainList!!.addAll(it.data)
//        binding?.driveList = alMainList

        for (i in alMainList!!.indices) {
            when (alMainList!!.get(i).Flag) {
                "answer" -> {
                    alAnswer?.add(alMainList!!.get(i))
                }
                "drive" -> {
                    alDrive?.add(alMainList!!.get(i))
                }
                "shared" -> {
                    alShared?.add(alMainList!!.get(i))
                }
            }
        }

        listSortByDate(alAnswer)//Sorting Drive list
        listSortByDate(alShared)//Sorting Share  list
        listSortByDate(alDrive)//Sorting Answer list

        setViewPagerStuff()

        if (alMainList?.size == 0) {
            viewModel.setDataFound(false)
        } else {
            viewModel.setDataFound(true)
        }
    }

    private fun listSortByDate(alDrive: ArrayList<DriveModel.Data>?) {
        Collections.sort(alDrive, object : Comparator<DriveModel.Data> {
            override fun compare(arg0: DriveModel.Data?, arg1: DriveModel.Data?): Int {
                val format = SimpleDateFormat("dd-MM-yy hh:mmaa")
                var compareResult = 0
                compareResult = try {
                    val fromDate = arg0?.filedate + " " + arg0?.filetime
                    val toDate = arg1?.filedate + " " + arg1?.filetime
                    val arg0Date = format.parse(fromDate)
                    val arg1Date = format.parse(toDate)
                    arg1Date.compareTo(arg0Date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                    arg0?.filedate!!.compareTo(arg1?.filedate!!)
                }
                return compareResult
            }
        })
    }

    interface DriveFragmentListner {
        fun popFragment()
        fun openAddToFragment()
    }

    fun closeDriveFragment() {
        listener?.popFragment()
    }

    fun openAddToDrive() {
        listener?.openAddToFragment()
    }

    override fun stateChanged() {
        getDriveList()
    }

    fun deleteDriveData(shareid: String, flag: String) {
        when (flag) {
            "answer" -> {
                answerfragment?.deleteDriveData(shareid)
            }
            "drive" -> {
                myDriveFragment?.deleteDriveData(shareid)
            }
            "shared" -> {
                sharedfragment?.deleteDriveData(shareid)
            }
        }


    }
}