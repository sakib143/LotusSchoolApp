package com.appforschool.ui.profile.user

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.ChangeProfilePicModel
import com.appforschool.data.model.UpdateProfileModel
import com.appforschool.databinding.ActivityUserProfileBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import com.yalantis.ucrop.UCrop
import java.io.File
import javax.inject.Inject

class UserProfileActivity : BaseBindingActivity<ActivityUserProfileBinding>() {

    @Inject
    lateinit var viewModel: UserProfileViewModel

    override fun layoutId() = R.layout.activity_user_profile

    override fun initializeBinding(binding: ActivityUserProfileBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, UserProfileActivity::class.java)
    }

    private fun setObserver() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.update_profile.observe(this, updateProfileObserver)
        viewModel.change_photo.observe(this, changeProfilePics)
    }

    private val changeProfilePics = Observer<ChangeProfilePicModel> {
        toast(it!!.message)
    }

    private val updateProfileObserver = Observer<UpdateProfileModel> {
        toast(it!!.message)
        if (it.status) {
            closeScreen()
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    fun closeScreen() {
        LogM.e("=> closeScreen is calling User Profile screen !!!")
        UserProfileListner.getInstance().changeState(true)
        finish()
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*")
            .addCategory(Intent.CATEGORY_OPENABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }

        startActivityForResult(Intent.createChooser(intent, "Select picture"), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val selectedUri = data!!.data
            if (selectedUri != null) {
                startCrop(selectedUri)
            } else {
                toast("Unable to retrieve image")
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            handleCropResult(data!!)
        }

        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data!!)
        }
    }

    private fun startCrop(uri: Uri) {
        val options = UCrop.Options()
        options.setFreeStyleCropEnabled(true)
        options.setHideBottomControls(true)

        val destinationFileName = ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop.withOptions(options)
        uCrop.start(this@UserProfileActivity)
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            toast("Something went wrong.Please try again")
        } else {
            toast("Something went wrong.Please try again")
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
        viewModel.selectedBitmapImage.value = bitmap
        viewModel.imagePath.value = File(resultUri?.path)
        viewModel.executeChangeProfilePic()
    }
}