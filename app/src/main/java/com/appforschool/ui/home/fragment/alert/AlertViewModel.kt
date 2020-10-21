package com.appforschool.ui.home.fragment.alert

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.repository.AlertRepository
import com.appforschool.data.repository.ScheduleRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class AlertViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: AlertRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _alertData : MutableLiveData<AlertModel> = MutableLiveData<AlertModel>()
    val alertData: LiveData<AlertModel>
        get() = _alertData

    private val _isDataFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isDataFound: LiveData<Boolean>
        get() = _isDataFound

    fun setDataFound(isFound: Boolean) {
        _isDataFound.value = isFound
    }

    fun executeAlert(): LiveData<AlertModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_GET_ALERT)
                inputParam.addProperty(Constant.REQUEST_STUDENTID,prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REUQEST_USER_ID,prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_USER_TYPE, prefUtils.getUserData()?.usertype)

                _isViewLoading.postValue(true)
                val apiResponse = repository.callAlert(inputParam)
                _isViewLoading.postValue(false)
                _alertData.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _alertData!!
    }

}