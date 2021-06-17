package com.appforschool.ui.home

import android.Manifest
import android.content.*
import android.content.BroadcastReceiver
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.opensooq.supernova.gligar.GligarPicker
import com.yalantis.ucrop.UCrop
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.home_nav_drawer.*
import org.jitsi.meet.sdk.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
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

    //Multiple Image selection START
    private val alMultiImage: ArrayList<MultiImageModel> = arrayListOf();
    private var selected_image = 1;
    //Multiple Image selection END

    //Add to PDF START
    var file: File? = null
    var fileOutputStream: FileOutputStream? = null
    val pdfDocument = PdfDocument()
    //Add to PDF END

    private var driveFragment: DriveFragment? = null

    //Jitsi Video calling related stuff START
    private var userName: String? = null
    private var isHost: Int = 1
    private var scheduleId: Int = 0
    private var roomUrl: String? = null
    private var roomId: String = ""
    //Jitsi Video calling related stuff END

    private var scheduleFragment: ScheduleFragment? = null

    override fun initializeBinding(binding: ActivityHomeBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.homeActivityListner = this
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        UserProfileListner.getInstance().setListener(this@HomeActivity)
        val model = UserProfileListner.getInstance().state

        dashboardFragment = DashboardFragment.newInstance()
        navigateToDashBoardFragment(false)
        setObserver()

        file = getOutputFile()
        fileOutputStream = FileOutputStream(file)
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
        viewModel.deleteDrive.observe(this@HomeActivity, deleteDriveObserver)
        viewModel.call_end_log.observe(this, callEndObserver)
        viewModel.inActiveSchedule.observe(this, inactiveScheduleObserver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                PICKFILE_RESULT_CODE -> {
                    val filePath = ImageFilePath.getPath(this@HomeActivity, data?.data)
                    val file: File = File(filePath)
                    fileUpload(file)
                }
                MULTI_IMAGE_PICKER_RESULT_CODE -> {
                    uploadMultiImageFile(data)
                }
                UCrop.REQUEST_CROP -> {
                    handleCropResult(data!!)
                }
                else -> {
                    handleCropError(data!!)
                }
            }
        }
    }

    private fun fileUpload(file: File) {
        toast(resources.getString(R.string.file_uploading_starting))
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
        scheduleFragment = ScheduleFragment.newInstance()
        addFragment(
            supportFragmentManager, scheduleFragment!!,
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
        driveFragment = DriveFragment.newInstance()
        addFragment(
            supportFragmentManager,
            driveFragment!!,
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

    private val inactiveScheduleObserver = Observer<InActiveModel> {
        toast(it.message)
    }

    private val callEndObserver = Observer<SetCallEndLogModel> {
        toast(it!!.message)
    }

    private val deleteDriveObserver = Observer<DeleteDriveModel> {
        if(it.status) {
            toast(it.data.get(0).message)
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

    fun permissionForVideoCalling(model: ScheduleModel.Data) = runWithPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
        if (model.meetinglink.isNullOrBlank()) {
            if(scheduleId != 0) {
                toast("Please end current meeting.")
            } else {
                viewModel.executeSetJoinLog(model.schid.toString())
                roomUrl = prefUtils.getUserData()?.videoserverurlnew
                roomId = model.roomno
                scheduleId =  model.schid
                setJitsiMeet()
            }
        } else {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.meetinglink))
                startActivity(browserIntent)
            }catch (e: Exception) {
                toast(e.message!!)
            }
//            try {
//                viewModel.executeSetJoinLog(model.schid.toString())
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.meetinglink))
//                if (intent.resolveActivity(packageManager) != null) {
//                    startActivity(intent)
//                }
//            } catch (e: Exception) {
//                toast(e.message)
//            }
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
            AlertDialogUtility.CustomAlert(this@HomeActivity,
                getString(R.string.app_name),
                getString(R.string.select_file),
                getString(R.string.image_file_title),
                getString(R.string.other_file_title),
                { dialog, which ->
                    dialog.dismiss()
                    GligarPicker().limit(4).disableCamera(false).cameraDirect(false).requestCode(
                        MULTI_IMAGE_PICKER_RESULT_CODE
                    ).withActivity(this).show()
                },
                { dialog, which ->
                    dialog.dismiss()
                    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                    chooseFile.setType("*/*")
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
                })
        }

    override fun openAssignmentFile(imageView: ImageView, model: AssignmentModel.Data) {
        viewModel.executeFileViewLog(model.shareid, "A")
        if(model.filetype.equals("F",ignoreCase = true)) {
            if (model.fileext.equals(".mp4", ignoreCase = true)) {
                if (!model.filepath.isNullOrEmpty()) {
                    val intent = Intent(this@HomeActivity, VideoPlayingActivity::class.java)
                    intent.putExtra(Constant.VIDEO_URL, model.filepath)
                    startActivity(intent)
                } else {
                    toast("File path not found.")
                }
            } else if (model.fileext.equals(".jpg", ignoreCase = true)
                || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
                    ".jpeg", ignoreCase = true)
            ) {
                if (!model.filepath.isNullOrEmpty()) {
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
                    toast("File path not found.")
                }
            } else {
                if (!model.filepath.isNullOrEmpty()) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.filepath))
                    startActivity(browserIntent)
                } else {
                    toast("Link URL not found.")
                }
            }
        } else if (model.filetype.equals("L",ignoreCase = true)) {
            var url: String = model.linkurl!!
            if (!url.isNullOrEmpty()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            } else {
                toast("Link URL not found.")
            }
        } else if (model.filetype.equals("N",ignoreCase = true)) {
            toast("No Attachment found.")
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
            AlertDialogUtility.CustomAlert(this@HomeActivity,
                getString(R.string.exam_start_title),
                getString(R.string.exam_start_message),
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

    override fun openScheduleMenu(view: View,model: ScheduleModel.Data) {
        val popupMenu: PopupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.schedule_meeting_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionShareLinkMeeting -> {
                    if (model.meetinglink.isNullOrBlank()) {
                       val  roomUrl = prefUtils.getUserData()?.videoserverurlnew  + model.roomno
                        globalMethods.shareTextToFriend(this@HomeActivity, "Share Link", roomUrl)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(model.meetinglink))
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                }
                R.id.actionInActiveMeeting -> {
                    viewModel.callInActiveSchedule(model.schid)
                    scheduleFragment?.removeSchedule(model.schid)
                }
            }
            true
        })
        popupMenu.show()
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

    override fun shareDriveData(view: View, model: DriveModel.Data) {
        val popupMenu: PopupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.drive_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionShare -> {
                    if (model.linkurl == null) {
                        globalMethods.shareTextToFriend(this@HomeActivity, "Share Drive file", model.filepath)
                    } else {
                        globalMethods.shareTextToFriend(this@HomeActivity, "Share Drive file", model.linkurl)
                    }
                }
                R.id.actionDelete -> {
                    driveFragment?.deleteDriveData(model.shareid, model.Flag)
                    viewModel.executeDeleteDrive(model.shareid, model.Flag)
                }
            }
            true
        })
        popupMenu.show()
    }

    override fun stateChanged() {
        dashboardFragment?.callHomeAPI()
    }

    //Multiple Image selection START
    private fun uploadMultiImageFile(data: Intent?) {
        val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)
        val selectedUri = Uri.fromFile(File(imagesList?.get(0)))
        alMultiImage.clear()
        for (j in imagesList!!.indices) {
            val mUri = Uri.fromFile(File(imagesList?.get(j)))
            alMultiImage.add(MultiImageModel(mUri, false))
        }

        if (selectedUri != null) {
            selected_image = 1
            startCrop(selectedUri)
        } else {
            Toast.makeText(
                this@HomeActivity,
                "R.string.toast_cannot_retrieve_selected_image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startCrop(uri: Uri) {
        val options = UCrop.Options()
        options.setFreeStyleCropEnabled(true)
        options.setHideBottomControls(true)

        val destinationFileName = ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop.withOptions(options)
        uCrop.start(this@HomeActivity)
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Log.e("=> ", "handleCropError: ", cropError)
            Toast.makeText(this@HomeActivity, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@HomeActivity, "Something went wrong", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            LogM.e("Cropped Images path is " + resultUri.path)
            when (selected_image) {
                1 -> {
                    setFirstImage(resultUri)
                }
                2 -> {
                    setSecondImage(resultUri)
                }
                3 -> {
                    setThirdImage(resultUri)
                }
                4 -> {
                    setFourthImage(resultUri)
                }
            }
            if (selected_image == alMultiImage.size + 1) {
                fileUpload(file!!)
            }
        } else {
            toast(getString(R.string.try_again_crop))
        }
    }

    private fun setFourthImage(resultUri: Uri) {
        selected_image = 5
        alMultiImage.get(3).imageUri = resultUri
        alMultiImage.get(3).isSelected = true
        if (alMultiImage.size > 4) {
            startCrop(alMultiImage.get(4).imageUri)
        }
    }

    private fun setThirdImage(resultUri: Uri) {
        selected_image = 4
        alMultiImage.get(2).imageUri = resultUri
        alMultiImage.get(2).isSelected = true
        if (alMultiImage.size > 3) {
            startCrop(alMultiImage.get(3).imageUri)
        }
    }

    private fun setSecondImage(resultUri: Uri) {
        selected_image = 3
        alMultiImage.get(0).imageUri = resultUri
        alMultiImage.get(0).isSelected = true
        addToPdf(1)
        if (alMultiImage.size > 2) {
            startCrop(alMultiImage.get(2).imageUri)
        }

    }

    private fun setFirstImage(resultUri: Uri) {
        selected_image = 2
        alMultiImage.get(0).imageUri = resultUri
        alMultiImage.get(0).isSelected = true
        addToPdf(0)
        if (alMultiImage.size > 1) {
            startCrop(alMultiImage.get(1).imageUri)
        }

    }

    fun addToPdf(position: Int) {
        val bitmap = BitmapFactory.decodeFile(alMultiImage.get(position).imageUri.path)
        val pageInfo =
            PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, position + 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.setColor(Color.BLUE)
        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)
        bitmap.recycle()
        pdfDocument.writeTo(fileOutputStream)
        LogM.e("PDF path is " + file?.absolutePath)
    }

    private fun getOutputFile(): File? {
        val root = File(getExternalFilesDir(null), "WhiteBoardLab")
        var isFolderCreated = true
        if (!root.exists()) {
            isFolderCreated = root.mkdir()
        }

        return if (isFolderCreated) {
            File(root, "whiteboard_images.pdf")
        } else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show()
            null
        }
    }
    //Multiple Image selection END

    //Jist Meet Video calling related stuff by Sakib START

    // Screen sharing relates stuff by Sakib Syed START
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.getType()) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> {
                    LogM.e("CONFERENCE_JOINED")
                }
                BroadcastEvent.Type.PARTICIPANT_JOINED -> {
                    LogM.e("PARTICIPANT_JOINED")
                }
                BroadcastEvent.Type.CONFERENCE_TERMINATED -> {
                    viewModel.executeSetEndcallLog(scheduleId)
                    roomId = ""
                    scheduleId = 0
                    LogM.e("CONFERENCE_TERMINATED")
                }
            }
        }
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    // Screen sharing relates stuff by Sakib Syed END

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    private fun setJitsiMeet() {
        val serverURL: URL
        serverURL = try {
            URL(roomUrl)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }

        var builder = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setWelcomePageEnabled(false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("meeting-name.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setFeatureFlag("video-share.enabled", false)   // Hide Youtube option from list.
            .setFeatureFlag("close-captions.enabled", false) // Hide Start showing subtitles from list.

        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = userName
        builder.setUserInfo(jitsiMeetUserInfo)


        if(prefUtils.getUserData()?.isrecordingoption == 0) {
            builder.setFeatureFlag("recording.enabled",false)
        }

        //Camera off when user is NOT host
        if (isHost == 0) {
            builder.setVideoMuted(true)
        }

        JitsiMeet.setDefaultConferenceOptions(builder.build())
        registerForBroadcastMessages()

        builder.build()

        val options = org.jitsi.meet.sdk.JitsiMeetConferenceOptions.Builder()
            .setRoom(roomId)
            .build()
        JitsiMeetActivity.launch(this, options)
    }

    //Jist Meet Video calling related stuff by Sakib END

}