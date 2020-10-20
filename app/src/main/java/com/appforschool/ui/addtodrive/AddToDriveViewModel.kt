package com.appforschool.ui.addtodrive

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.data.repository.LoginRepository
import com.appforschool.utils.GlobalMethods
import javax.inject.Inject

class AddToDriveViewModel @Inject constructor(
    private val application: MyApp,
    private val globalMethods: GlobalMethods,
    private val repository: LoginRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    var title =  MutableLiveData<String>()
    var description =  MutableLiveData<String>()
    var filetype =  MutableLiveData<String>()

}