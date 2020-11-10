package com.appforschool.ui.profile.user

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.UpdateProfileModel
import com.appforschool.data.repository.UpdateProfileRepository
import com.appforschool.utils.*
import com.google.gson.JsonObject
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

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val emailAddress = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()

    init {
        firstName.postValue(prefUtils.getUserData()?.firstname)
        lastName.postValue(prefUtils.getUserData()?.lastname)
        emailAddress.postValue(prefUtils.getUserData()?.emailid)
        phoneNumber.postValue(prefUtils.getUserData()?.phone1)
    }

    //Update Profile observer related data
    private val _update_profile: MutableLiveData<UpdateProfileModel> =
        MutableLiveData<UpdateProfileModel>()
    val update_profile: LiveData<UpdateProfileModel>
        get() = _update_profile


    fun updateProfile() {
        if (isValidate()) {
            executeUpdateProfile()
        }
    }

    private fun isValidate(): Boolean {
        if (!globalMethods.isInternetAvailable(application.applicationContext)) {
            _onMessageError.postValue("Please check you internet.")
            return false
        } else if (firstName.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter first name.")
            return false
        } else if (lastName.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter last name.")
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
                inputParam.addProperty(Constant.REQUEST_LAST_NAME, lastName.value)
                inputParam.addProperty(Constant.REQUEST_FIRST_NAME, firstName.value)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REQUEST_PHONE_1, phoneNumber.value)
                inputParam.addProperty(Constant.REQUEST_EMAIL_ID, emailAddress.value)
                inputParam.addProperty(Constant.REQUEST_USER_TYPE, prefUtils.getUserData()?.usertype)
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

}