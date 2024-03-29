package com.appforschool.ui.home

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.BuildConfig
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.*
import com.appforschool.data.repository.HomeActivityRepository
import com.appforschool.utils.*
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class HomeActivitViewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val repository: HomeActivityRepository,
    private val globalMethods: GlobalMethods
) : AndroidViewModel(application) {

    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private var _starndard = MutableLiveData<String>()
    val starndard: LiveData<String>
        get() = _starndard

    private var _schoolLogo = MutableLiveData<String>()
    val schoolLogo: LiveData<String>
        get() = _schoolLogo

    val versionName = BuildConfig.VERSION_NAME

    init {
        _userName.postValue(prefUtils.getUserData()?.studentname)
        _starndard.postValue(prefUtils.getUserData()?.standardname)
        _schoolLogo.postValue(prefUtils.getUserData()?.logofilepath)
    }

//    fun getUserData(strUserName: String, strStandard: String,strLogo: String ) {
//        LogM.e("=> User name $strUserName strStandard $strStandard strLogo $strLogo ")
//        _userName.postValue(strUserName)
//        _starndard.postValue(strStandard)
//        _schoolLogo.postValue(strLogo)
//    }

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    //Video calling log related stuff
    private val _setIsJoinLog: MutableLiveData<SetJoinModel> = MutableLiveData<SetJoinModel>()
    val setIsJoinLog: LiveData<SetJoinModel>
        get() = _setIsJoinLog

    //Video calling log related stuff
    private val _deleteDrive: MutableLiveData<DeleteDriveModel> = MutableLiveData<DeleteDriveModel>()
    val deleteDrive: LiveData<DeleteDriveModel>
        get() = _deleteDrive

    //Open file log related stuff
    private val _fileViewLog: MutableLiveData<FileViewLogModel> =
        MutableLiveData<FileViewLogModel>()
    val fileViewLog: LiveData<FileViewLogModel>
        get() = _fileViewLog

    //Uplolad assignment
    val filePath = MutableLiveData<File>()
    private val _fileSubmit: MutableLiveData<AssignmentSubmissionModel> =
        MutableLiveData<AssignmentSubmissionModel>()
    val fileSubmit: LiveData<AssignmentSubmissionModel>
        get() = _fileSubmit

    //Start Exam observer related data
    private val _startExam: MutableLiveData<StartExamModel> =
        MutableLiveData<StartExamModel>()
    val startExam: LiveData<StartExamModel>
        get() = _startExam

    //View Result observer related data
    private val _viewResult: MutableLiveData<ViewResultModel> =
        MutableLiveData<ViewResultModel>()
    val viewResult: LiveData<ViewResultModel>
        get() = _viewResult

    fun executeSetJoinLog(scheduleId: String): LiveData<SetJoinModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_SET_JOIN_LOG)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REQUEST_SCHEDULE_ID, scheduleId)
                inputParam.addProperty(Constant.REQUEST_DEVICE_SMALL, Constant.KEY_ANDROID)
                val apiResponse = repository.callSetJoinLog(inputParam)
                _setIsJoinLog.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _setIsJoinLog!!
    }

    fun executeDeleteDrive(driveId: String, strFlag: String): LiveData<DeleteDriveModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.MODE_DELETE_DRIVE)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REQUEST_USER_TYPE, prefUtils.getUserData()?.usertype)
                inputParam.addProperty(Constant.REQUEST_DRIVE_ID, driveId)
                when (strFlag) {
                    "answer" -> {
                        inputParam.addProperty(Constant.REQUEST_FLAG, 3)
                    }
                    "drive" -> {
                        inputParam.addProperty(Constant.REQUEST_FLAG, 1)
                    }
                    "shared" -> {
                        inputParam.addProperty(Constant.REQUEST_FLAG, 2)
                    }
                }

                val apiResponse = repository.callDeleteDrive(inputParam)
                _deleteDrive.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _deleteDrive!!
    }

    fun executeFileViewLog(shareId: String, viewType: String): LiveData<FileViewLogModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_SETFILE_VIEW_LOG)
                inputParam.addProperty(Constant.REQUEST_VIEW_TYPE, viewType)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(
                    Constant.REQUEST_STUDENTID,
                    prefUtils.getUserData()?.studentId
                )
                inputParam.addProperty(
                    Constant.REQUEST_USER_TYPE,
                    prefUtils.getUserData()?.usertype
                )
                inputParam.addProperty(Constant.REUQEST_SHARE_ID, shareId)
                inputParam.addProperty(Constant.REQUEST_DEVICE_SMALL, Constant.KEY_ANDROID)
                val apiResponse = repository.callFileViewLogLog(inputParam)
                _fileViewLog.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _fileViewLog!!
    }


    fun uploadAssignmentFile(
        shareId: String,
        fileTitle: String,
        fileDesc: String,
        fileText: String,
        fileSize: String,
        uploadType: String
    ): LiveData<AssignmentSubmissionModel> {
        Coroutines.main {
            val a = "A"
            val fileReqBodyLicense =
                filePath.value?.asRequestBody("image/*".toMediaTypeOrNull())
            val userImageBody = MultipartBody.Part.createFormData(
                Constant.REQUEST_IMAGE,
                filePath.value?.name,
                fileReqBodyLicense!!
            )
            val shareid = shareId.toRequestBody("text/plain".toMediaTypeOrNull())
            val userid =
                prefUtils.getUserData()!!.userid?.toRequestBody("text/plain".toMediaTypeOrNull())
            val usertype =
                prefUtils.getUserData()!!.usertype?.toRequestBody("text/plain".toMediaTypeOrNull())
            val studentid =
                prefUtils.getUserData()!!.studentId?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filetitle = fileTitle.toRequestBody("text/plain".toMediaTypeOrNull())
            val filedescr = fileDesc.toRequestBody("text/plain".toMediaTypeOrNull())
            val filetype = a.toRequestBody("text/plain".toMediaTypeOrNull())
            val fileext = fileText.toRequestBody("text/plain".toMediaTypeOrNull())
            val filesize = fileSize.toRequestBody("text/plain".toMediaTypeOrNull())
            val uploadtype = uploadType.toRequestBody("text/plain".toMediaTypeOrNull())
            try {
                val response = repository.callUploadAssignment(
                    userImageBody,
                    shareid,
                    userid,
                    usertype,
                    studentid,
                    filetitle,
                    filedescr,
                    filetype,
                    fileext,
                    filesize,
                    uploadtype
                )
                _fileSubmit.postValue(response)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _fileSubmit!!
    }


    fun executeStartExam(examId: String): LiveData<StartExamModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_MODE_START_EXAM)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_EXAM_ID, examId)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                val apiResponse = repository.callStartExam(inputParam)
                _startExam.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _startExam!!
    }

    fun executeViewResult(examId: String): LiveData<ViewResultModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_MODE_VIEW_RESULT)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_EXAM_ID, examId)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                val apiResponse = repository.callViewResult(inputParam)
                _viewResult.postValue(apiResponse)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _viewResult!!
    }

}