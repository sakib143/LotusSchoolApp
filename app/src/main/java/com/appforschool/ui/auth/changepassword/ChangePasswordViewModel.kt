package com.appforschool.ui.auth.changepassword

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.data.repository.LoginRepository
import com.appforschool.utils.GlobalMethods
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val application: MyApp,
    private val globalMethods: GlobalMethods,
    private val repository: LoginRepository
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


    fun executeChangePassword() {

    }

}