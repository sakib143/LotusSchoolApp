package com.learnathome.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import com.learnathome.R
import com.learnathome.base.BaseBindingActivity
import com.learnathome.data.model.ScheduleModel
import com.learnathome.databinding.ActivityHomeBinding
import com.learnathome.di.Injectable
import com.learnathome.listner.HomeListner
import com.learnathome.ui.home.fragment.HomeFragment
import com.learnathome.utils.toast
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseBindingActivity<ActivityHomeBinding>(),
    Injectable,
    HasAndroidInjector,
    HomeFragment.HomeListener, HomeListner {

    override fun layoutId() = R.layout.activity_home

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @Inject
    lateinit var viewModel: HomeActivitViewModel

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>


    private var homeFragment: HomeFragment? = null

    override fun initializeBinding(binding: ActivityHomeBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.homeActivityListner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeFragment = HomeFragment.newInstance()
        navigateToHomeFragment(false)

    }

    private fun navigateToHomeFragment(addToBackStack: Boolean) {
        if (supportFragmentManager.findFragmentByTag(HomeFragment::class.java.name) != null) {
            repeat(supportFragmentManager.fragments.size) {
                if (supportFragmentManager.findFragmentById(R.id.container) !is HomeFragment) {
                    supportFragmentManager.popBackStackImmediate()
                }
            }
        } else {
            addFragmentWithoutAnimation(
                supportFragmentManager, homeFragment!!,
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

    override fun popFragment() {
        onBackPressed()
    }

    override fun openHamBurgerMenu() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun performLogout() {
        prefUtils.clearAll()
        navigationController.navigateToLoginScreen(this@HomeActivity)
    }

    override fun openVideoCalling(model: ScheduleModel.Data) {
        navigationController.navigateToVideoCallScreen(this@HomeActivity, model.meetinglink)
    }

}