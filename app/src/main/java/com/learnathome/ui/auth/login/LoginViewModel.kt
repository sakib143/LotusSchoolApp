package com.learnathome.ui.auth.login

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.learnathome.MyApp
import com.learnathome.api.ApiExceptions
import com.learnathome.api.NoInternetException
import com.learnathome.data.model.LoginModel
import com.learnathome.data.repository.LoginRepository
import com.learnathome.utils.Constant
import com.learnathome.utils.Coroutines
import com.learnathome.utils.GlobalMethods
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val application: MyApp,
    private val globalMethods: GlobalMethods,
    private val repository: LoginRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _login_data: MutableLiveData<LoginModel> =
        MutableLiveData<LoginModel>()
    val login_data: LiveData<LoginModel>
        get() = _login_data

    var userid =  MutableLiveData<String>()
    var password = MutableLiveData<String>()


    private val _isShowPassword = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isShowPassword : LiveData<Boolean> get() = _isShowPassword

    fun setHideShowPassword() {
        _isShowPassword.value = _isShowPassword.value != true
    }

    fun executeLogin(): LiveData<LoginModel> {
        Coroutines.main {
            if (isValidate()) {
                val loginParam = JsonObject()
                loginParam.addProperty(Constant.REQUEST_USERNAME, userid.value)
                loginParam.addProperty(Constant.REQUEST_PASSWORD, password.value)
                try {
                    _isViewLoading.postValue(true)
                    val apiResponse = repository.callLogin(loginParam)
                    _isViewLoading.postValue(false)
                    _login_data.postValue(apiResponse)
                } catch (e: ApiExceptions) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(e.message)
                } catch (e: NoInternetException) {
                    _isViewLoading.postValue(false)
                    _onMessageError.postValue(e.message)
                }
            }
        }
        return _login_data
    }

    private fun isValidate(): Boolean {
        if (!globalMethods.isInternetAvailable(application.applicationContext)) {
            _onMessageError.postValue("Please check you internet")
            return false
        } else if (userid.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter email id")
            return false
        } else if (password.value.isNullOrEmpty()) {
            _onMessageError.postValue("Please enter password")
            return false
        }
        return true
    }


}