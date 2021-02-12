package com.appforschool.ui.home.fragment.drive.mydrive

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.data.model.DriveModel
import com.appforschool.data.repository.DriveRepository
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class MyDriveViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
) : AndroidViewModel(application) {

    private val _isNoDataFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isNoDataFound: LiveData<Boolean>
        get() = _isNoDataFound


    init {
        _isNoDataFound.postValue(true)
    }

    fun setNoDataFound(isDataLoad: Boolean) {
        _isNoDataFound.postValue(isDataLoad)
    }


}