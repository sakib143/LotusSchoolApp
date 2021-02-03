package com.appforschool.ui.home

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.appforschool.BuildConfig
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.*
import com.appforschool.databinding.ActivityHomeBinding
import com.appforschool.di.Injectable
import com.appforschool.listner.HomeListner
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.full_image.FullExamDateActivity
import com.appforschool.ui.full_image.FullImageActivity
import com.appforschool.ui.home.fragment.assignment.AssignmentFragment
import com.appforschool.ui.home.fragment.dashboard.DashboardFragment
import com.appforschool.ui.home.fragment.drive.DriveFragment
import com.appforschool.ui.home.fragment.exam.ExamListFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import com.appforschool.ui.home.fragment.subject.SubjectFragment
import com.appforschool.ui.home.fragment.subject.subjectdetails.SubjectDetailsFragment
import com.appforschool.ui.videoplaying.VideoPlayingActivity
import com.appforschool.utils.*
import com.appforschool.utils.circle_imageview.CircularImageView
import com.bumptech.glide.Glide
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_nav_drawer.*
import java.io.File
import javax.inject.Inject


class HomeActivity : BaseBindingActivity<ActivityHomeBinding>(),
    Injectable,
    HasAndroidInjector,
    ScheduleFragment.HomeListener, HomeListner, DashboardFragment.FragmentListner,
    SubjectFragment.SubjectFragmentListner, SubjectDetailsFragment.SubjectDetailsListner,
    AssignmentFragment.AssignmentFragmentListner,
    com.appforschool.ui.home.fragment.notification.AlertFragment.AlertListner,
    ExamListFragment.ExamListListner, DriveFragment.DriveFragmentListner,
    UserProfileListner.onScreenCloseListner {

    override fun layoutId() = R.layout.activity_home

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @Inject
    lateinit var viewModel: HomeActivitViewModel

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private var dashboardFragment: DashboardFragment? = null
    private var shareId: String = ""

    override fun initializeBinding(binding: ActivityHomeBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.homeActivityListner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        UserProfileListner.getInstance().setListener(this@HomeActivity)
        val model = UserProfileListner.getInstance().state

        dashboardFragment = DashboardFragment.newInstance()
        navigateToDashBoardFragment(false)
        setObserver()
    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context) =
            Intent(
                context,
                HomeActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    private fun setObserver() {
        viewModel.setIsJoinLog.observe(this@HomeActivity, joinLogObserver)
        viewModel.startExam.observe(this@HomeActivity, startExamObserver)
        viewModel.viewResult.observe(this@HomeActivity, viewResultObserver)
        viewModel.fileViewLog.observe(this@HomeActivity, fileViewLogObserver)
        viewModel.fileSubmit.observe(this@HomeActivity, fileSubmitObserver)
        viewModel.onMessageError.observe(this@HomeActivity, onMessageErrorObserver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null) {
            when (requestCode) {
                PICKFILE_RESULT_CODE -> {
                    toast(resources.getString(R.string.file_uploading_starting))
                    val filePath = ImageFilePath.getPath(this@HomeActivity, data?.data)
                    val file: File = File(filePath)
                    viewModel.filePath.value = file
                    val fileSizeInBytes = file.length()
                    val fileSizeInKB = fileSizeInBytes / 1024
                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString())
                    val strFileExtension: String = "." + fileExtension
                    viewModel.uploadAssignmentFile(
                        shareId,
                        "title",
                        "description",
                        strFileExtension,
                        fileSizeInKB.toString(),
                        "A"
                    )
                }
            }
        }
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
                supportFragmentManager,
                dashboardFragment!!,
                addToBackStack = addToBackStack
            )
        }
    }

    fun openMyProfile() {
        navigationController.navigateToStudentProfile(this@HomeActivity)
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

    fun openChangePassword() {
        navigationController.navigateToChangePassword(this@HomeActivity)
    }

    fun openInWeb() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.WEB_URL))
        startActivity(browserIntent)
    }

    fun signOut() {
        prefUtils.clearAll()
        navigationController.navigateToLoginScreen(this@HomeActivity)
    }

    override fun popFragment() {
        onBackPressed()
    }

    override fun openAddToFragment() {
        navigationController.navigateToAddToDrive(this@HomeActivity)
    }

    override fun openSubjectDetails(subjectID: String, subjectName: String) {
        addFragment(
            supportFragmentManager,
            SubjectDetailsFragment.newInstance(subjectID, subjectName),
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

    override fun openAlertFragment() {
        addFragment(
            supportFragmentManager,
            com.appforschool.ui.home.fragment.notification.AlertFragment.newInstance(),
            addToBackStack = true
        )
    }

    override fun openExamListFragment() {
        addFragment(
            supportFragmentManager,
            ExamListFragment.newInstance(),
            addToBackStack = true
        )
    }

    override fun openDriveFragment() {
        addFragment(
            supportFragmentManager,
            DriveFragment.newInstance(),
            addToBackStack = true
        )
    }

    override fun updateUserName(strUserName: String, strStandard: String, strLogo: String) {
        tvUserNameHA.setText(strUserName)
        tvStandardHA.setText(strStandard)
        Glide.with(this@HomeActivity)
            .load(strLogo)
            .into(ivLogoHA)
//        viewModel.getUserData(strUserName,strStandard,strLogo)
    }

    override fun openProfileScreen() {
        navigationController.navigateToStudentProfile(this@HomeActivity)
    }

    override fun openFullImage(imgProfilePic: CircularImageView) {
        ViewCompat.setTransitionName(imgProfilePic, Constant.IMAGE_FULL_ZOOM_ANIM)
        val intent = Intent(this, FullImageActivity::class.java)
        intent.putExtra(Constant.REQUEST_LINK_URL, prefUtils.getUserData()!!.ProfileImage)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imgProfilePic!!,
            ViewCompat.getTransitionName(imgProfilePic)!!
        )
        startActivity(intent, options.toBundle())
    }

    private val joinLogObserver = Observer<SetJoinModel> {
        if (it.status) {
        } else {
            toast(it.message)
        }
    }

    private val startExamObserver = Observer<StartExamModel> {
        if (it.status) {
            navigationController.navigateToAttendExam(
                this@HomeActivity,
                it.data.get(0).examid,
                it.data.get(0).examname!!,
                it.data.get(0).subjectname!!,
                it.data.get(0).totalmarks.toString()!!,
                it.data.get(0).duration.toString()!!,
                it.data.get(0).examtime!!,
                it.data.get(0).examStartDateTime!!,
                false,
                ""
            )
        } else {
            toast(it!!.message)
        }
    }

    private val viewResultObserver = Observer<ViewResultModel> {
        if (it.status) {
            navigationController.navigateToAttendExam(
                this@HomeActivity,
                it.data.get(0).examid,
                it.data.get(0).examname!!,
                it.data.get(0).subjectname!!,
                it.data.get(0).totalmarks.toString()!!,
                it.data.get(0).duration.toString()!!,
                it.data.get(0).examtime!!,
                it.data.get(0).examStartDateTime!!,
                true,
                it.data.get(0).totalobtainedmarks.toString()
            )
        } else {
            toast(it!!.message)
        }
    }

    private val fileViewLogObserver = Observer<FileViewLogModel> {
        if (it.status) {
            toast(it.message)
        } else {
            toast(it.message)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val fileSubmitObserver = Observer<AssignmentSubmissionModel> {
        if (it.status) {
//            openAssignmentFragment()
            toast(it.message)
        } else {
            toast(it.message)
        }
    }

    override fun openVideoCalling(model: ScheduleModel.Data) {
        permissionForVideoCalling(model)
    }

    fun permissionForVideoCalling(model: ScheduleModel.Data) = runWithPermissions(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    ) {
        if (model.meetinglink.isNullOrBlank()) {
            viewModel.executeSetJoinLog(model.schid.toString())
            val fullUrl = prefUtils.getUserData()?.videoserverurl + model.schid
            val scheduleId = model.schid
            navigationController.navigateToVideoCallScreen(this@HomeActivity, fullUrl, scheduleId)
        } else {
            viewModel.executeSetJoinLog(model.schid.toString())
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.meetinglink))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun openSubjectFile(imageView: ImageView, model: SubjectDetailsModel.Data) {
        if (model.fileext.equals(".mp4", ignoreCase = true)) {
            val intent = Intent(this@HomeActivity, VideoPlayingActivity::class.java)
            intent.putExtra(Constant.VIDEO_URL, model.filepath)
            startActivity(intent)
        } else if (model.fileext.equals(
                ".jpg",
                ignoreCase = true
            ) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
                ".jpeg",
                ignoreCase = true
            )
        ) {
            ViewCompat.setTransitionName(imageView, Constant.IMAGE_FULL_ZOOM_ANIM)
            val intent = Intent(this, FullImageActivity::class.java)
            intent.putExtra(Constant.REQUEST_LINK_URL, model.filepath)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageView!!,
                ViewCompat.getTransitionName(imageView)!!
            )
            startActivity(intent, options.toBundle())
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.filepath))
            startActivity(browserIntent)
        }
    }

    override fun openSubjectDetails(model: ScheduleModel.Data) {
        addFragment(
            supportFragmentManager,
            SubjectDetailsFragment.newInstance(model.subjid.toString(), model.courseName),
            addToBackStack = true
        )
    }

    override fun openSubmissionFile(model: AssignmentModel.Data) {
        checkFileSubmitPermission(model)
    }

    fun checkFileSubmitPermission(model: AssignmentModel.Data) =
        runWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE) {
            shareId = model.shareid
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.setType("*/*")
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
        }

    override fun openAssignmentFile(imageView: ImageView, model: AssignmentModel.Data) {
        viewModel.executeFileViewLog(model.shareid, "A")
        if (model.fileext.equals(".mp4", ignoreCase = true)) {
            val intent = Intent(this@HomeActivity, VideoPlayingActivity::class.java)
            intent.putExtra(Constant.VIDEO_URL, model.filepath)
            startActivity(intent)
        } else if (model.fileext.equals(
                ".jpg",
                ignoreCase = true
            ) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
                ".jpeg",
                ignoreCase = true
            )
        ) {
            ViewCompat.setTransitionName(imageView, Constant.IMAGE_FULL_ZOOM_ANIM)
            val intent = Intent(this, FullImageActivity::class.java)
            intent.putExtra(Constant.REQUEST_LINK_URL, model.filepath)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                imageView!!,
                ViewCompat.getTransitionName(imageView)!!
            )
            startActivity(intent, options.toBundle())
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.filepath))
            startActivity(browserIntent)
        }
    }

    override fun openAlertDetails(model: AlertModel.Data) {
        toast("Coming soon")
    }

    override fun openExamDetails(model: ExamModel.Data) {
        if (model.isshowviewresult == 1) {
            LogM.e("=> view exam id " + model.examid)
            viewModel.executeViewResult(model.examid)
        } else {
            AlertDialogUtility.CustomAlert(this@HomeActivity, getString(R.string.exam_start_title), getString(R.string.exam_start_message),
                "Yes",
                "No",
                { dialog, which ->
                    dialog.dismiss()
                    viewModel.executeStartExam(model.examid)
                },
                { dialog, which ->
                    dialog.dismiss()
                })
        }
    }

    override fun openExamDetailsZoom(linearLayout: LinearLayout, date: String, time: String) {
        ViewCompat.setTransitionName(linearLayout, Constant.IMAGE_FULL_ZOOM_ANIM)
        val intent = Intent(this, FullExamDateActivity::class.java)
        intent.putExtra(Constant.REQUEST_MODE_START_EXAM, date)
        intent.putExtra(Constant.REQUEST_MODE_END_EXAM, time)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, linearLayout!!, ViewCompat.getTransitionName(
                linearLayout
            )!!
        )
        startActivity(intent, options.toBundle())
    }

    override fun openDriveList(imageView: ImageView, model: DriveModel.Data) {
        viewModel.executeFileViewLog(model.shareid, "D")
        if (model.linkurl.isNullOrEmpty()) {
            if (model.fileext.equals(".mp4", ignoreCase = true)) {
                val intent = Intent(this@HomeActivity, VideoPlayingActivity::class.java)
                intent.putExtra(Constant.VIDEO_URL, model.filepath)
                startActivity(intent)
            } else if (model.fileext.equals(
                    ".jpg",
                    ignoreCase = true
                ) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
                    ".jpeg",
                    ignoreCase = true
                )
            ) {
                ViewCompat.setTransitionName(imageView, Constant.IMAGE_FULL_ZOOM_ANIM)
                val intent = Intent(this, FullImageActivity::class.java)
                intent.putExtra(Constant.REQUEST_LINK_URL, model.filepath)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    imageView!!,
                    ViewCompat.getTransitionName(imageView)!!
                )
                startActivity(intent, options.toBundle())
            } else {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.filepath))
                startActivity(browserIntent)
            }
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.linkurl))
            startActivity(browserIntent)
        }
    }

    override fun shareDriveData(model: DriveModel.Data) {
        if (model.linkurl == null) {
            globalMethods.shareTextToFriend(this@HomeActivity, "Share Drive file", model.filepath)
        } else {
            globalMethods.shareTextToFriend(this@HomeActivity, "Share Drive file", model.linkurl)
        }
    }

    override fun stateChanged() {
        LogM.e("=> stateChanged is calling !!!")
        dashboardFragment?.callHomeAPI()
    }
}