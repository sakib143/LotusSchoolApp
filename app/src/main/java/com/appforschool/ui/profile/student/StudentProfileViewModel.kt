package com.appforschool.ui.profile.student

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.data.repository.LoginRepository
import com.appforschool.utils.GlobalMethods
import javax.inject.Inject

class StudentProfileViewModel @Inject constructor(
    private val application: MyApp,
    private val globalMethods: GlobalMethods
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val emailAddress = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()


    fun updateProfile() {
        if(isValidate()){

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

}