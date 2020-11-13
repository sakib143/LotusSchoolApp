package com.appforschool.ui.profile.user

import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.R
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.ChangeProfilePicModel
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.UpdateProfileModel
import com.appforschool.data.repository.UpdateProfileRepository
import com.appforschool.utils.*
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val application: MyApp,
    private val globalMethods: GlobalMethods,
    private val prefUtils: PrefUtils,
    private val repository: UpdateProfileRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    //Change profile pic related data
    private val _change_photo: MutableLiveData<ChangeProfilePicModel> =
        MutableLiveData<ChangeProfilePicModel>()
    val change_photo: LiveData<ChangeProfilePicModel>
        get() = _change_photo

    //Update Profile observer related data
    private val _update_profile: MutableLiveData<UpdateProfileModel> =
        MutableLiveData<UpdateProfileModel>()
    val update_profile: LiveData<UpdateProfileModel>
        get() = _update_profile

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val emailAddress = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()

    val userPlaceHolder = R.mipmap.ic_launcher
    val imagePath = MutableLiveData<File>()
    val selectedBitmapImage = MutableLiveData<Bitmap>().apply {
        value =
            globalMethods.drawableToBitmap(application.getDrawable(R.mipmap.ic_launcher)!!)
    }

    init {
        firstName.postValue(prefUtils.getUserData()?.firstname)
        lastName.postValue(prefUtils.getUserData()?.lastname)
        emailAddress.postValue(prefUtils.getUserData()?.emailid)
        phoneNumber.postValue(prefUtils.getUserData()?.phone1)
    }

    fun updateProfile() {
        if (isValidate()) {
            Coroutines.main {
                if (imagePath.value == null) {
                    executeUpdateProfile()
                } else {
                    executeUpdateProfile()
                    executeChangeProfilePic()
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        val emailId = emailAddress.value?.trim() // Taken another variable for trime
        if (!globalMethods.isInternetAvailable(application.applicationContext)) {
            _onMessageError.postValue("Please check you internet.")
            return false
        } else if (firstName.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter first name.")
            return false
        } else if (lastName.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter last name.")
            return false
        } else if (!emailId.isNullOrEmpty() && !emailId.isEmailValid()) {
            _onMessageError.postValue("Please enter valid email address.")
            return false
        } else if (phoneNumber.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter mobile number.")
            return false
        } else if (phoneNumber.value.toString().length < 10) {
            _onMessageError.postValue("Please enter valid mobile number.")
            return false
        }
        return true
    }

    fun executeUpdateProfile(): LiveData<UpdateProfileModel> {
        Coroutines.main {
            if (isValidate()) {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_USER_PROFILE)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_LAST_NAME, lastName.value?.trim())
                inputParam.addProperty(Constant.REQUEST_FIRST_NAME, firstName.value?.trim())
                inputParam.addProperty(
                    Constant.REQUEST_STUDENTID,
                    prefUtils.getUserData()?.studentId
                )
                inputParam.addProperty(Constant.REQUEST_PHONE_1, phoneNumber.value?.trim())
                inputParam.addProperty(Constant.REQUEST_EMAIL_ID, emailAddress.value?.trim())
                inputParam.addProperty(
                    Constant.REQUEST_USER_TYPE,
                    prefUtils.getUserData()?.usertype
                )
                try {
                    _isViewLoading.postValue(true)
                    val apiResponse = repository.callUpdateProfile(inputParam)
                    _isViewLoading.postValue(false)
                    _update_profile.postValue(apiResponse)
                } catch (e: ApiExceptions) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(e.message)
                } catch (e: NoInternetException) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(e.message)
                }
            }
        }
        return _update_profile
    }

    fun executeChangeProfilePic(): LiveData<ChangeProfilePicModel> {
        Coroutines.main {
            val fileReqBodyLicense =
                imagePath.value?.asRequestBody("image/*".toMediaTypeOrNull())
            val userImageBody = MultipartBody.Part.createFormData(
                Constant.REQUEST_IMAGE,
                imagePath.value?.name,
                fileReqBodyLicense!!
            )
            val userIdBody = prefUtils.getUserId()?.toRequestBody("text/plain".toMediaTypeOrNull())

            try {
                _isViewLoading.postValue(true)
                val response = repository.callChnageProfilePic(userImageBody, userIdBody!!)
                _isViewLoading.postValue(false)
                _change_photo.postValue(response)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _change_photo!!
    }

}