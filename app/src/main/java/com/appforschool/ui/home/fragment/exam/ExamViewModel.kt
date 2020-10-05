package com.appforschool.ui.home.fragment.exam

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.ExamModel
import com.appforschool.data.repository.AlertRepository
import com.appforschool.data.repository.ExamRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class ExamViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: ExamRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _examData : MutableLiveData<ExamModel> = MutableLiveData<ExamModel>()
    val examData: LiveData<ExamModel>
        get() = _examData

    fun executeExamData(): LiveData<ExamModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_GET_EXAMS)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserId())
                _isViewLoading.postValue(true)
                val apiResponse = repository.callExam(inputParam)
                _isViewLoading.postValue(false)
                _examData.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _examData!!
    }

}