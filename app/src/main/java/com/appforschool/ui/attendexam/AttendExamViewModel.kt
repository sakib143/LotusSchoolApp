package com.appforschool.ui.attendexam

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.R
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.UpdateExamAnswerModel
import com.appforschool.data.repository.AttendExamRepository
import com.appforschool.utils.Constant
import com.appforschool.utils.Coroutines
import com.appforschool.utils.PrefUtils
import com.appforschool.utils.toast
import com.google.gson.JsonObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
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

    //AttendExam observer related data
    private val _update_answer: MutableLiveData<UpdateExamAnswerModel> =
        MutableLiveData<UpdateExamAnswerModel>()
    val update_answer: LiveData<UpdateExamAnswerModel>
        get() = _update_answer

    //Getting Exam id
    private val _examId: MutableLiveData<String> =
        MutableLiveData<String>()
    val examId: LiveData<String>
        get() = _examId

    //Display if data not found
    private val _isDataFound: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val isDataFound: LiveData<Boolean>
        get() = _isDataFound

    private val _examName: MutableLiveData<String> =
        MutableLiveData<String>()
    val examName: LiveData<String>
        get() = _examName

    private val _subjectName: MutableLiveData<String> =
        MutableLiveData<String>()
    val subjectName: LiveData<String>
        get() = _subjectName

    private val _time: MutableLiveData<String> =
        MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    private val _duration: MutableLiveData<String> =
        MutableLiveData<String>()
    val duration: LiveData<String>
        get() = _duration

    private val _marks: MutableLiveData<String> =
        MutableLiveData<String>()
    val marks: LiveData<String>
        get() = _marks

    private val _formatedDate: MutableLiveData<String> =
        MutableLiveData<String>()
    val formatedDate: LiveData<String>
        get() = _formatedDate

    //Set Five minute left observer
    private val _setFiveMinuteLeft: MutableLiveData<String> =
        MutableLiveData<String>()
    val setFiveMinuteLeft: LiveData<String>
        get() = _setFiveMinuteLeft

    //Set Time Over observer
    private val _setTimeOver: MutableLiveData<String> =
        MutableLiveData<String>()
    val setTimeOver: LiveData<String>
        get() = _setTimeOver



    fun setData(examId: String,examName: String, subject: String,makrs: String, duration: String,time: String,formatedTime: String) {
        _examId.postValue(examId)
        _examName.postValue(examName)
        _subjectName.postValue(subject)
        _marks.postValue(makrs)
        _duration.postValue(duration)
        _time.postValue(time)
        _formatedDate.postValue(formatedTime)
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

    fun executeUpdateExamAnswer(srNo: String,objectAnswer:String, subjectiveAnswer:String): LiveData<UpdateExamAnswerModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_EXAM_ID, examId.value)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REQUEST_SR_NO, srNo)
                inputParam.addProperty(Constant.REQUEST_OBJECTIVE_ANSWER, objectAnswer)
                inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, subjectiveAnswer)
                val apiResponse = repository.callUpdateExamAnswer(inputParam)
                _update_answer.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            }catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _update_answer!!
    }

    fun oneMinuteLeftAlert() {
        Coroutines.main {
            try {
                var duration: Int = duration.value!!.toInt()
                val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
                val date = dateFormat.parse(_formatedDate.value)
                val calendar = Calendar.getInstance()
                calendar!!.time = date
                calendar!!.add(Calendar.MINUTE, duration - 1)
                val latestDate = calendar.time
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            application.toast(application.resources.getString(R.string.exam_min_left_second_message))
                        }, 1000)
                    }
                }, latestDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }

    fun fiveMinuteLeftAlert(): LiveData<String> {
        Coroutines.main {
            try {
                var duration: Int = duration.value!!.toInt()
                val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
                val date = dateFormat.parse(_formatedDate.value)
                val calendar = Calendar.getInstance()
                calendar!!.time = date
                calendar!!.add(Calendar.MINUTE, duration - 5)
                val latestDate = calendar.time
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            _setFiveMinuteLeft.postValue("")
                        }, 1000)
                    }
                }, latestDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return _setFiveMinuteLeft!!
    }

    fun timeOverAlert(): LiveData<String> {
        Coroutines.main {
            try {
                var duration: Int = duration.value!!.toInt()
                val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
                val date = dateFormat.parse(_formatedDate.value)
                val calendar = Calendar.getInstance()
                calendar!!.time = date
                calendar!!.add(Calendar.MINUTE, duration)
                val latestDate = calendar.time
                val timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        Handler(Looper.getMainLooper()).postDelayed({
                            application.toast(application.resources.getString(R.string.exam_time_over))
                            _setTimeOver.postValue("")
                    }, 5000)
                    }
                }, latestDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return _setTimeOver!!
    }

}