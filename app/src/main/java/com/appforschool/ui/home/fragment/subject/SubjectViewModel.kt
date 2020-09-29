package com.appforschool.ui.home.fragment.subject

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.SubjectModel
import com.appforschool.data.repository.ScheduleRepository
import com.appforschool.data.repository.SubjectRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.security.auth.Subject

class SubjectViewModel   @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: SubjectRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _subjectData : MutableLiveData<SubjectModel> =
        MutableLiveData<SubjectModel>()
    val subjectData: LiveData<SubjectModel>
        get() = _subjectData

    fun executerSubject() : LiveData<SubjectModel> {
        Coroutines.main {
            val inputParam = JsonObject()
            inputParam.addProperty(Constant.REQUEST_STUDENTID,prefUtils.getUserId())

            try {
                _isViewLoading.postValue(true)
                val apiResponse = repository.callSubject(inputParam)
                _isViewLoading.postValue(false)
                _subjectData.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _subjectData!!
    }


}