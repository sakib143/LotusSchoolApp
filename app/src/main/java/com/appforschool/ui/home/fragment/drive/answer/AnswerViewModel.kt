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

    private val _isDataFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isDataFound: LiveData<Boolean>
        get() = _isDataFound

    init {
        _isDataFound.postValue(true)
    }

    fun setDataLoad(isDataLoad: Boolean) {
        _isDataFound.postValue(isDataLoad)
    }

}