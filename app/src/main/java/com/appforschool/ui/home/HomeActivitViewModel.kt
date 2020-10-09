package com.appforschool.ui.home

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.GetVersionModel
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.model.SetJoinModel
import com.appforschool.data.repository.DriveRepository
import com.appforschool.data.repository.HomeActivityRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.GlobalMethods
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class HomeActivitViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: HomeActivityRepository
) : AndroidViewModel(application)  {

    private var _userName = MutableLiveData<String>()
    val userName : LiveData<String>
        get() = _userName

    private var _starndard = MutableLiveData<String>()
    val starndard : LiveData<String>
        get() = _starndard

    fun getUserData() {
        _userName.value = prefUtils.getUserData()?.studentname
        _starndard.value = prefUtils.getUserData()?.standardname
    }

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _setIsJoinLog : MutableLiveData<SetJoinModel> = MutableLiveData<SetJoinModel>()
    val setIsJoinLog: LiveData<SetJoinModel>
        get() = _setIsJoinLog

    fun executeSetJoinLog(scheduleId: String): LiveData<SetJoinModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_SET_JOIN_LOG)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REQUEST_SCHEDULE_ID, scheduleId)
                inputParam.addProperty(Constant.REQUEST_DEVICE, Constant.KEY_ANDROID)
                val apiResponse = repository.callSetJoinLog(inputParam)
                _setIsJoinLog.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            }catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _setIsJoinLog!!
    }

}