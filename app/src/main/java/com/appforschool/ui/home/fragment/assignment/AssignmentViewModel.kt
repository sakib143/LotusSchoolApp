package com.appforschool.ui.home.fragment.assignment

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.data.repository.AssignmentRepository
import com.appforschool.data.repository.SubjectDetailsRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class AssignmentViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: AssignmentRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    private val _subjectDetails: MutableLiveData<AssignmentModel> =
        MutableLiveData<AssignmentModel>()
    val subjectDetails: LiveData<AssignmentModel>
        get() = _subjectDetails

    fun executerAssigment(): LiveData<AssignmentModel> {
        Coroutines.main {
            val inputParam = JsonObject()
            inputParam.addProperty(Constant.REQUEST_MODE, Constant.REUQEST_MODE_GET_ASSIGNMENT)
            inputParam.addProperty(Constant.REUQEST_USER_ID,prefUtils.getUserData()?.userid)
            inputParam.addProperty(Constant.REQUEST_USER_TYPE, prefUtils.getUserData()?.usertype)
            inputParam.addProperty(Constant.REQUEST_STUDENTID,prefUtils.getUserData()?.studentId)

            try {
                _isViewLoading.postValue(true)
                val apiResponse = repository.callAssignment(inputParam)
                _isViewLoading.postValue(false)
                _subjectDetails.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _subjectDetails!!
    }


}