package com.appforschool.ui.home.fragment.dashboard

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingFragment
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.HomeApiModel
import com.appforschool.data.model.LoginModel
import com.appforschool.databinding.FragmentDashboardBinding
import com.appforschool.ui.home.fragment.schedule.ScheduleViewModel
import com.appforschool.utils.AlertDialogUtility
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import javax.inject.Inject

class DashboardFragment : BaseBindingFragment<FragmentDashboardBinding>() {

    private var listener: FragmentListner? = null

    override fun layoutId(): Int = R.layout.fragment_dashboard

    @Inject
    lateinit var viewModel: DashboardviewModel

    override fun initializeBinding(binding: FragmentDashboardBinding) {
        binding.lifecycleOwner = this
        binding.listner = this
        binding.viewmodel = viewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(): DashboardFragment {
            val dashBoardFragment = DashboardFragment()
            val bundle = Bundle().apply {}
            dashBoardFragment.arguments = bundle
            return dashBoardFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListner) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getUserName()

        viewModel.onMessageError.observe(viewLifecycleOwner, onMessageErrorObserver)
        viewModel.homeAPI.observe(viewLifecycleOwner, homeAPIObserver)
        //Make API call
        if (globalMethods.isInternetAvailable(activity!!)) {
            viewModel.executeHomeAPI()
        } else {
            activity!!.toast(Constant.CHECK_INTERNET)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        activity?.toast(it.toString())
    }

    private val homeAPIObserver = Observer<HomeApiModel> {
        if (it.status) {
            //Save data to Session Manager OR Pref Utils
            val loginModel = LoginModel.Data()
            loginModel.studentId = it.data.get(0).studentId.toString()
            loginModel.studentname = it.data.get(0).studentname
            loginModel.standardname = it.data.get(0).standardname
            loginModel.ishost = it.data.get(0).ishost
            loginModel.usertype = it.data.get(0).usertype
            loginModel.userid = prefUtils.getUserData()!!.userid
            loginModel.standardid = it.data.get(0).standardid
            loginModel.currentversion = it.data.get(0).currentversion
            loginModel.isforceupdate = it.data.get(0).isforceupdate
            loginModel.firstname = it.data.get(0).firstname
            loginModel.lastname = it.data.get(0).lastname
            loginModel.emailid = it.data.get(0).emailid
            loginModel.phone1 = it.data.get(0).phone1
            prefUtils.saveUserId(prefUtils.getUserData()!!.userid,loginModel)
            //Refresh User name from session manager.
            viewModel.getUserName()
            checkLatestVersion(it)
        }
    }

    private fun checkLatestVersion(it: HomeApiModel) {
        val latestVersion = it.data.get(0).currentversion
        val currentVersion = globalMethods.getAppVersion(activity!!)
        val isForceUpdate = it.data.get(0).isforceupdate

        if (!latestVersion.equals(currentVersion, ignoreCase = true) && isForceUpdate.equals(
                "yes",
                ignoreCase = true
            )
        ) {
            prefUtils.clearAll()
            AlertDialogUtility.showSingleAlert(
                activity!!, "Please update latest version."
            ) { dialog, which ->
                dialog.dismiss()
                try {
                    val appStoreIntent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=${activity!!.packageName}")
                        )
                    appStoreIntent.setPackage("com.android.vending")
                    startActivity(appStoreIntent)
                } catch (exception: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$this.packageName")
                        )
                    )
                }
            }
        }
    }

    interface FragmentListner {
        fun popFragment()
        fun openScheduleFragment()
        fun openHamBurgerMenu()
        fun openSubjectFragment()
        fun openAssignmentFragment()
        fun openAlertFragment()
        fun openExamListFragment()
        fun openDriveFragment()
    }

    fun openScheduleFragment() {
        listener?.openScheduleFragment()
    }

    fun openHamBurgerMenu() {
        listener?.openHamBurgerMenu()
    }

    fun openSubjectFragment() {
        listener?.openSubjectFragment()
    }

    fun openAssignment() {
        listener?.openAssignmentFragment()
    }

    fun openAlert() {
        listener?.openAlertFragment()
    }

    fun openExamFragment() {
        listener?.openExamListFragment()
    }

    fun openDriveFragment() {
        listener?.openDriveFragment()
    }

}