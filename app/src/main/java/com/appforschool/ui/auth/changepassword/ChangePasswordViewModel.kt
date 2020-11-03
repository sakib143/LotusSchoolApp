package com.appforschool.ui.auth.changepassword

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.ChangePasswordModel
import com.appforschool.data.model.LoginModel
import com.appforschool.data.repository.ChangePasswordRepository
import com.appforschool.data.repository.LoginRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.GlobalMethods
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val application: MyApp,
    private val globalMethods: GlobalMethods,
    private val repository: ChangePasswordRepository,
    private val prefUtils: PrefUtils
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _oldPassworddisplay = MutableLiveData<Boolean>()
    val oldPassworddisplay: LiveData<Boolean> get() = _oldPassworddisplay

    private val _newPassworddisplay = MutableLiveData<Boolean>()
    val newPassworddisplay: LiveData<Boolean> get() = _newPassworddisplay

    private val _confirmPassworddisplay = MutableLiveData<Boolean>()
    val confirmPassworddisplay: LiveData<Boolean> get() = _confirmPassworddisplay

    private val _change_password_api: MutableLiveData<ChangePasswordModel> =
        MutableLiveData<ChangePasswordModel>()
    val change_password_api: LiveData<ChangePasswordModel>
        get() = _change_password_api

    fun setOldPasswordVisiblity() {
        _oldPassworddisplay.value = _oldPassworddisplay.value != true
    }

    fun setNewPasswordVisiblity() {
        _newPassworddisplay.value = _newPassworddisplay.value != true
    }

    fun setConfirmPasswordVisiblity() {
        _confirmPassworddisplay.value = _confirmPassworddisplay.value != true
    }

    var oldPassword = MutableLiveData<String>()
    var newPassword = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()


    fun executeChangePassword(): LiveData<ChangePasswordModel>  {
        Coroutines.main {
            if (isValidate()) {
                val loginParam = JsonObject()
                loginParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_CHANGE_PASSWORD)
                loginParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                loginParam.addProperty(Constant.REQUEST_OLD_PASSWORD, oldPassword.value)
                loginParam.addProperty(Constant.REQUEST_NEW_PASSWORD, newPassword.value)
                try {
                    _isViewLoading.postValue(true)
                    val apiResponse = repository.callChangePasswordd(loginParam)
                    _isViewLoading.postValue(false)
                    _change_password_api.postValue(apiResponse)
                } catch (e: ApiExceptions) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(e.message)
                } catch (e: NoInternetException) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(e.message)
                }
            }
        }
        return _change_password_api
    }

    private fun isValidate(): Boolean {
        if (!globalMethods.isInternetAvailable(application.applicationContext)) {
            _onMessageError.postValue("Please check you internet.")
            return false
        } else if (oldPassword.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter old password.")
            return false
        } else if (newPassword.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter new password.")
            return false
        } else if (confirmPassword.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter confirm password.")
            return false
        } else if (newPassword.value != confirmPassword.value) {
            _onMessageError.postValue("New password and confirm password should be same.")
            return false
        }
        return true
    }

}