package com.appforschool.ui.videocalling

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.SetCallEndLogModel
import com.appforschool.data.repository.VideoCallingRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class VideoCallingViewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: VideoCallingRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    //Call End log observer related data
    private val _call_end_log: MutableLiveData<SetCallEndLogModel> =
        MutableLiveData<SetCallEndLogModel>()
    val call_end_log: LiveData<SetCallEndLogModel>
        get() = _call_end_log

    fun executeSetEndcallLog(scheduleId: String): LiveData<SetCallEndLogModel> {
        Coroutines.main {
            val inputParam = JsonObject()
            inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_SET_END_MEETING_LOG)
            inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
            inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
            inputParam.addProperty(Constant.REQUEST_SCHEDULE_ID, scheduleId)
            inputParam.addProperty(Constant.REQUEST_DEVICE_SMALL, Constant.KEY_ANDROID)
            try {
                _isViewLoading.postValue(true)
                val apiResponse = repository.callEndLog(inputParam)
                _isViewLoading.postValue(false)
                _call_end_log.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _call_end_log
    }

}