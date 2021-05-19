package com.appforschool.ui.home.fragment.drive.answer

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class AnswerViewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
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