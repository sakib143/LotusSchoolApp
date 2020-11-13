package com.appforschool.ui.profile.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.ChangeProfilePicModel
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.UpdateProfileModel
import com.appforschool.databinding.ActivityUserProfileBinding
import com.appforschool.utils.toast
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import javax.inject.Inject

class UserProfileActivity :  BaseBindingActivity<ActivityUserProfileBinding>() {

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
        viewModel.change_photo.observe(this,changeProfilePics)
    }

    private val changeProfilePics = Observer<ChangeProfilePicModel> {
        toast(it!!.message)
    }

    private val updateProfileObserver = Observer<UpdateProfileModel> {
        toast(it!!.message)
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    fun closeScreen() {
        finish()
    }

    fun openGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this@UserProfileActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val imageUri = result.uri
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                viewModel.selectedBitmapImage.value = bitmap
                viewModel.imagePath.value = File(result.uri.path)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}