package com.appforschool.ui.home.fragment.drive.shared

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class ShareDriveViewModel  @Inject constructor(
    private val application: MyApp,
) : AndroidViewModel(application) {

    private val _isNoDataFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNoDataFound: LiveData<Boolean>
        get() = _isNoDataFound

    init {
        _isNoDataFound.postValue(true)
    }

    fun setNoDataFound(isDataFound: Boolean) {
        _isNoDataFound.postValue(isDataFound)
    }


}