package com.appforschool.ui.home.fragment.dashboard

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.data.repository.ScheduleRepository
import com.appforschool.utils.LogM
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class DashboardviewModel   @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils
) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _standard = MutableLiveData<String>()
    val standard: LiveData<String> get() = _standard

    fun getUserName() {
        LogM.e("=> User name in viewmodel is " + prefUtils.getUserData()?.studentname)
        _userName.value = prefUtils.getUserData()?.studentname
        _standard.value = prefUtils.getUserData()?.standardname
    }

}