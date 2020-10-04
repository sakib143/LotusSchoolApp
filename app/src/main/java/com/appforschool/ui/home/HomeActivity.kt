package com.appforschool.ui.home

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.appforschool.BuildConfig
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.databinding.ActivityHomeBinding
import com.appforschool.di.Injectable
import com.appforschool.listner.HomeListner
import com.appforschool.ui.home.fragment.assignment.AssignmentFragment
import com.appforschool.ui.home.fragment.dashboard.DashboardFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import com.appforschool.ui.home.fragment.subject.SubjectFragment
import com.appforschool.ui.home.fragment.subject.subjectdetails.SubjectDetailsFragment
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject


class HomeActivity : BaseBindingActivity<ActivityHomeBinding>(),
    Injectable,
    HasAndroidInjector,
    ScheduleFragment.HomeListener, HomeListner, DashboardFragment.FragmentListner,
    SubjectFragment.SubjectFragmentListner, SubjectDetailsFragment.SubjectDetailsListner,
        AssignmentFragment.AssignmentFragmentListner
{

    override fun layoutId() = R.layout.activity_home

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @Inject
    lateinit var viewModel: HomeActivitViewModel

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    private var homeFragment: ScheduleFragment? = null

    override fun initializeBinding(binding: ActivityHomeBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.homeActivityListner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeFragment = ScheduleFragment.newInstance()
//        navigateToHomeFragment(false)
        navigateToDashBoardFragment(false)
        viewModel.getUserData()

    }

    private fun navigateToDashBoardFragment(addToBackStack: Boolean) {
        if (supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.name) != null) {
            repeat(supportFragmentManager.fragments.size) {
                if (supportFragmentManager.findFragmentById(R.id.container) !is DashboardFragment) {
                    supportFragmentManager.popBackStackImmediate()
                }
            }
        } else {
            addFragmentWithoutAnimation(
                supportFragmentManager, DashboardFragment.newInstance(),
                addToBackStack = addToBackStack
            )
        }
    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context) =
            Intent(
                context,
                HomeActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    fun openMyProfile() {
        toast("openMyProfile is clicked !!!")
    }

    fun shareAopToFriend() {
        val type = "text/plain"
        val title = resources.getString(R.string.app_name)
        val strBody = """
            ${resources.getString(R.string.share_to_friend)}
            http://play.google.com/store/apps/details?id=
            """.trimIndent()
        globalMethods.shareAppToFriend(this@HomeActivity, type, title, strBody)
    }

    fun openPlayStoreURL() {
        try {
            val appStoreIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${this.packageName}"))
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

    fun signOut() {
        prefUtils.clearAll()
        navigationController.navigateToLoginScreen(this@HomeActivity)
    }

    override fun popFragment() {
        onBackPressed()
    }

    override fun openSubjectDetails(subjectID: String) {
        addFragment(
            supportFragmentManager,
            SubjectDetailsFragment.newInstance(subjectID),
            addToBackStack = true
        )
    }

    override fun openScheduleFragment() {
        addFragment(
            supportFragmentManager,
            ScheduleFragment.newInstance(),
            addToBackStack = true
        )
    }

    override fun openHamBurgerMenu() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun openSubjectFragment() {
        addFragment(
            supportFragmentManager,
            SubjectFragment.newInstance(),
            addToBackStack = true
        )
    }

    override fun openAssignmentFragment() {
        addFragment(
            supportFragmentManager,
            AssignmentFragment.newInstance(),
            addToBackStack = true
        )
    }

    override fun openVideoCalling(model: ScheduleModel.Data) {
        if(model.meetinglink.isNullOrBlank()){
            val isHost:Int = prefUtils.getUserData()!!.ishost
            val fullUrl = BuildConfig.VIDEO_CALL_URL + model.schid
            LogM.e("=> video calling url " + fullUrl)
            navigationController.navigateToVideoCallScreen(
                this@HomeActivity,
                fullUrl,
                prefUtils.getUserData()?.studentname!!,
                isHost
            )
        }else{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.meetinglink))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun openSubjectFile(model: SubjectDetailsModel.Data) {
        toast("Open file is clicked")
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.Column2))
        startActivity(browserIntent)
    }

    override fun openAssignmentFile(model: AssignmentModel.Data) {
        toast("Open Assignment is clicked")
    }
}