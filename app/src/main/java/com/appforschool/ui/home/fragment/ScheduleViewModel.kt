package com.appforschool.ui.home.fragment

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.repository.ScheduleRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class ScheduleViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: ScheduleRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _scheduleData : MutableLiveData<ScheduleModel> = MutableLiveData<ScheduleModel>()
    val scheduleData: LiveData<ScheduleModel>
        get() = _scheduleData

    val studentName: String? = prefUtils.getUserName()

    fun executeScheduleData(): LiveData<ScheduleModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserId())
                _isViewLoading.postValue(true)
                val apiResponse = repository.callScheduleData(inputParam)
                _isViewLoading.postValue(false)
                _scheduleData.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _scheduleData!!
    }


}