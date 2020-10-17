package com.appforschool.ui.home

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.*
import com.appforschool.data.repository.DriveRepository
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
    private val repository: HomeActivityRepository
) : AndroidViewModel(application) {

    private var _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    private var _starndard = MutableLiveData<String>()
    val starndard: LiveData<String>
        get() = _starndard

    fun getUserData() {
        _userName.value = prefUtils.getUserData()?.studentname
        _starndard.value = prefUtils.getUserData()?.standardname
    }

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    //Video calling log related stuff
    private val _setIsJoinLog: MutableLiveData<SetJoinModel> = MutableLiveData<SetJoinModel>()
    val setIsJoinLog: LiveData<SetJoinModel>
        get() = _setIsJoinLog

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

    fun executeSetJoinLog(scheduleId: String): LiveData<SetJoinModel> {
        Coroutines.main {
            try {
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_SET_JOIN_LOG)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(
                    Constant.REQUEST_STUDENTID,
                    prefUtils.getUserData()?.studentId
                )
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


    fun uploadAssignmentFile(shareId: String,fileTitle: String,fileDesc: String,fileText: String, fileSize: String,uploadType:String): LiveData<AssignmentSubmissionModel> {
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
            val userid =  prefUtils.getUserData()!!.userid?.toRequestBody("text/plain".toMediaTypeOrNull())
            val usertype = prefUtils.getUserData()!!.usertype?.toRequestBody("text/plain".toMediaTypeOrNull())
            val studentid = prefUtils.getUserData()!!.studentId?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filetitle = fileTitle.toRequestBody("text/plain".toMediaTypeOrNull())
            val filedescr = fileDesc.toRequestBody("text/plain".toMediaTypeOrNull())
            val filetype = a.toRequestBody("text/plain".toMediaTypeOrNull())
            val fileext = fileText.toRequestBody("text/plain".toMediaTypeOrNull())
            val filesize = fileSize.toRequestBody("text/plain".toMediaTypeOrNull())
            val uploadtype = uploadType.toRequestBody("text/plain".toMediaTypeOrNull())
            try {
                val response = repository.callUploadAssignment(userImageBody, shareid,userid,usertype,studentid,filetitle,filedescr,filetype,fileext,filesize,uploadtype)
                _fileSubmit.postValue(response)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
            }
        }
        return _fileSubmit!!
    }
}