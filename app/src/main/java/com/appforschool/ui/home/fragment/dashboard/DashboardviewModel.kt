package com.appforschool.ui.home.fragment.dashboard

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.R
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.HomeApiModel
import com.appforschool.data.repository.DashboardRepository
import com.appforschool.data.repository.ScheduleRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.LogM
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class DashboardviewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: DashboardRepository
) : AndroidViewModel(application) {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _standard = MutableLiveData<String>()
    val standard: LiveData<String> get() = _standard

    private val _profilePicUrl = MutableLiveData<String>()
    val profilePicUrl: LiveData<String> get() = _profilePicUrl

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _homeAPI: MutableLiveData<HomeApiModel> = MutableLiveData<HomeApiModel>()
    val homeAPI: LiveData<HomeApiModel>
        get() = _homeAPI

    val userPlaceHolder = R.mipmap.ic_launcher

    init {
        _userName.postValue(prefUtils.getUserData()?.studentname)
        _standard.postValue(prefUtils.getUserData()?.standardname)
        _profilePicUrl.postValue(prefUtils.getUserData()?.ProfileImage)
    }

    fun getUserName() {
        _userName.value = prefUtils.getUserData()?.studentname
        _standard.value = prefUtils.getUserData()?.standardname
        _profilePicUrl.postValue(prefUtils.getUserData()?.ProfileImage)
    }

    fun executeHomeAPI(): LiveData<HomeApiModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_HOMEAPI)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_DEVICE_SMALL, Constant.KEY_ANDROID)
                _isViewLoading.postValue(true)
                val apiResponse = repository.callHomeData(inputParam)
                _isViewLoading.postValue(false)
                _homeAPI.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _homeAPI!!
    }

}