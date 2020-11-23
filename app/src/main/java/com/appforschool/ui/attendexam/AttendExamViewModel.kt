package com.appforschool.ui.attendexam

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.repository.AttendExamRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.google.gson.JsonObject
import javax.inject.Inject

class AttendExamViewModel  @Inject constructor(
    private val application: MyApp,
    private val repository: AttendExamRepository,
    private val prefUtils: PrefUtils
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    //AttendExam observer related data
    private val _attend_exam: MutableLiveData<AttendExamModel> =
        MutableLiveData<AttendExamModel>()
    val attend_exam: LiveData<AttendExamModel>
        get() = _attend_exam

    //Getting Exam id
    private val _examId: MutableLiveData<String> =
        MutableLiveData<String>()
    val examId: LiveData<String>
        get() = _examId

    //Display if data not found
    private val _isDataFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isDataFound: LiveData<Boolean>
        get() = _isDataFound


    fun setData(examId: String?) {
        _examId.postValue(examId)
    }

    fun setDataFound(isFound: Boolean) {
        _isDataFound.value = isFound
    }

    fun executeAttentExamList(): LiveData<AttendExamModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_GET_EXAM_QUESTION_BY_EXAM_ID)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_EXAM_ID, examId.value)
                val apiResponse = repository.callAttendExam(inputParam)
                _attend_exam.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            }catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _attend_exam!!
    }

}