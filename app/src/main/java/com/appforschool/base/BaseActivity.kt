package com.appforschool.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.appforschool.R
import com.appforschool.di.Injectable
import com.appforschool.utils.GlobalMethods
import com.appforschool.utils.NavigationController
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

/**
 * Class which will be extended by every Activity
 */
@Suppress("unused")
abstract class BaseActivity : AppCompatActivity(), Injectable {

    var AUTOCOMPLETE_REQUEST_CODE = 1

    /**
     * Object for @see [NavigationController], which is used to for navigating screens
     * */
    @Inject
    lateinit var navigationController: NavigationController

    /**
     * Object for @see [PrefUtils] for preferences storage
     * */
    @Inject
    lateinit var prefUtils: PrefUtils

    /**
     * Object for @see [ViewModelProvider.Factory]
     * */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Makeing GlobalMethods injecatable
     * */
    @Inject
    lateinit var globalMethods: GlobalMethods

    protected val PICKFILE_RESULT_CODE = 786
    protected val MULTI_IMAGE_PICKER_RESULT_CODE = 92
    protected val IMAGE_CROP_RESULT_CODE = 101

    /**
     * request code for storage permission
     * */
    private val mPermissionRequestStorage = 111

    /**
     * request code for camera permission
     * */
    private val mPermissionRequestCamera = 222
    /**
     * request code for location permission
     * */
    private val mPermissionRequestLocation = 333

    /**
     * To get Layout Resource ID
     * @return Layout ID
     */
    abstract fun layoutId(): Int

    /**
     * this method gets called when this activity gets created
     * Annotated @CallSuper to make mandatory super call to child class
     * */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
    }


    /**
     * One of the methods of Activity Life Cycle, this will be called after activity gets created
     * */
    override fun onResume() {
        super.onResume()
    }


//    /**
//     * Shows Alert dialog to notify user that they re no longer logged-in
//     */
//    fun notAuthorizedDialog() {
//        AlertDialog.Builder(this)
//            .setIcon(android.R.drawable.ic_dialog_alert)
//            .setCancelable(false)
//            .setTitle("Session Expired!")
//            .setMessage("Please login")
//            .setPositiveButton("OK") { _, _ ->
//                clearPrefsAndNavigateToLanding()
//            }
//            .show()
//    }

//    /**
//     * Navigates User to Login page by clearing current session
//     */
//    fun clearPrefsAndNavigateToLanding() {
//        val notificationManager =
//            applicationContext?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
//        notificationManager?.cancelAll()
//        prefUtils.clearAll()
//        navigationController.navigateToLandingPage(this@BaseActivity)
//        finishAffinity()
//    }

    /**
     * this method will add fragment with animation and backStack
     * @param supportFragmentManager -> supportFragmentManager
     * @param fragment -> fragment which needs to be added
     * */
    fun addFragment(
        supportFragmentManager: FragmentManager,
        fragment: Fragment,
        containerId: Int? = R.id.container, addToBackStack: Boolean = true
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit
        )
        containerId?.let { fragmentTransaction.add(it, fragment, fragment.javaClass.name) }
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.javaClass.name)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }



    /**
     * this method will add fragment without animation and backStack
     * @param supportFragmentManager -> supportFragmentManager
     * @param fragment -> fragment which needs to be added
     * */
    fun addFragmentWithoutAnimation(
        supportFragmentManager: FragmentManager?, fragment: Fragment,
        containerId: Int? = R.id.container, addToBackStack: Boolean = true
    ) {
        val fragmentTransaction = supportFragmentManager?.beginTransaction()
        containerId?.let { fragmentTransaction?.add(it, fragment, fragment.javaClass.name) }
        if (addToBackStack) {
            fragmentTransaction?.addToBackStack(fragment.javaClass.name)
        }
        fragmentTransaction?.commitAllowingStateLoss()
    }

    /**
     * Return true if locationPermission granted
     * */
    fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**o
     * for requesting location permission
     * */
    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            mPermissionRequestLocation
        )
    }
}