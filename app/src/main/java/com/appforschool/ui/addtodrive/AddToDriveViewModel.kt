package com.appforschool.ui.addtodrive

import android.view.View
import android.webkit.URLUtil.isValidUrl
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.R
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.*
import com.appforschool.data.repository.AddToDriveRepository
import com.appforschool.utils.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.adapter_drive.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class AddToDriveViewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val globalMethods: GlobalMethods,
    private val repository: AddToDriveRepository
) : AndroidViewModel(application) {

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> get() = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> get() = _onMessageError

    //Standard observer
    private val _standard: MutableLiveData<StandardListModel> =
        MutableLiveData<StandardListModel>()
    val standard: LiveData<StandardListModel>
        get() = _standard

    //Subject observer
    private val _subject: MutableLiveData<SubjectListModel> =
        MutableLiveData<SubjectListModel>()
    val subject: LiveData<SubjectListModel>
        get() = _subject

    //Set Starndard spinner visiblity
    private val _setStandardVisiblity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val setStandardVisiblity: LiveData<Boolean>
        get() = _setStandardVisiblity

    //Set Subject spinner visiblity
    private val _setSubjectVisiblity: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val setSubjectVisiblity: LiveData<Boolean>
        get() = _setSubjectVisiblity

    //Upload file link
    private val _uploadFileLink: MutableLiveData<UploadFileUrlModel> =
        MutableLiveData<UploadFileUrlModel>()
    val uploadFileLink: LiveData<UploadFileUrlModel>
        get() = _uploadFileLink

    //Upload selected File
    private val _upload_selected_file: MutableLiveData<AssignmentSubmissionModel> =
        MutableLiveData<AssignmentSubmissionModel>()
    val upload_selected_file: LiveData<AssignmentSubmissionModel>
        get() = _upload_selected_file


    // value for uploading files
    val topic = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val standardid = MutableLiveData<Int>()
    val subjectId = MutableLiveData<Int>()
    val filetype = MutableLiveData<String>()
    val kwtype = MutableLiveData<String>()
    val fileext = MutableLiveData<String>()
    val filesize = MutableLiveData<String>()
    val linkurl = MutableLiveData<String>()
    val file = MutableLiveData<File>()


    private val _isFileSelected = MutableLiveData<Boolean>()
    val isFileSelected: LiveData<Boolean> get() = _isFileSelected

    val alKnowledge: ArrayList<KnwledgeTypeModel> = repository.knowledgeTypeList()

    fun setFileSelect(isSelected: Boolean) {
        _isFileSelected.value = isSelected
    }

    fun checkUserType() {
        if (prefUtils.getUserData()?.usertype.equals("S", ignoreCase = true)) {
            _setStandardVisiblity.value = false
            _setSubjectVisiblity.value = true
            standardid.value = prefUtils.getUserData()?.standardid
            executerSubjectList(prefUtils.getUserData()?.standardid.toString())
        } else {
            _setStandardVisiblity.value = true
            _setSubjectVisiblity.value = true
            executeStandardList()
        }
    }

    fun executerSubjectList(studentId: String): LiveData<SubjectListModel> {
        Coroutines.main {
            try {
                _isViewLoading.postValue(true)
                val inputParam = JsonObject()
                inputParam.addProperty(
                    Constant.REQUEST_MODE,
                    Constant.REQUEST_GETSUBJECTSBYSTANDARD
                )
                inputParam.addProperty(Constant.REQUEST_STANDARDID, studentId)
                val apiResponse = repository.callSubjectListForAddDrive(inputParam)
                _subject.postValue(apiResponse)
                _isViewLoading.postValue(false)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
                _isViewLoading.postValue(false)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
                _isViewLoading.postValue(false)
            }
        }
        return _subject!!
    }

    fun executeStandardList(): LiveData<StandardListModel> {
        Coroutines.main {
            try {
                _isViewLoading.postValue(true)
                val inputParam = JsonObject()
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_GETALLSTANDARDS)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                val apiResponse = repository.callStandardListForAddDrive(inputParam)
                _standard.postValue(apiResponse)
                _isViewLoading.postValue(false)
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
                _isViewLoading.postValue(false)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _standard!!
    }

    fun onKnoledgeSelection(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        LogM.e("=> testing " + pos)
        kwtype.value = alKnowledge.get(pos).id
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
    }

    fun onStandardSelection(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        LogM.e("=> testing " + pos)
        standardid.value = _standard.value?.data?.get(pos)?.groupid
        executerSubjectList(_standard.value?.data?.get(pos)?.groupid.toString())
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
    }

    fun onSubjectSelection(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        subjectId.value = _subject.value?.data?.get(pos)?.courseid!!
        LogM.e("=> testing " + pos)
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getAdapter().getCount()      get item count
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        //and other...
    }

    fun uploadToDrive() {
        if (checkValidation()) {
            if (isFileSelected.value == true) {
                callFileAddDrive()
            } else {
                if (checkValidation()) {
                    executerUploadFileUrlModelDrive()
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        var isValid = false
        if (topic.value.isNullOrEmpty()) {
            application.toast("Please add topic")
        } else if (description.value.isNullOrEmpty()) {
            application.toast("Please add description")
        } else if (kwtype.value == null) {
            application.toast("Please select knowledge type")
        } else if (isFileSelected.value == true && file.value == null) {
            application.toast("Please choose file")
        } else if (isFileSelected.value == false && linkurl.value.isNullOrEmpty()) {
            application.toast("Please enter url")
        } else if (isFileSelected.value == false && !globalMethods.isValidUrl(linkurl.value.toString())) {
            application.toast("Please enter valid URL")
        } else {
            isValid = true
        }
        return isValid
    }

    fun executerUploadFileUrlModelDrive(): LiveData<UploadFileUrlModel> {
        Coroutines.main {
            try {
                _isViewLoading.postValue(true)
                val inputParam = uploadLInkParam()
                val apiResponse = repository.callLinkAddDrive(inputParam)
                _uploadFileLink.postValue(apiResponse)
                _isViewLoading.postValue(false)
                if (apiResponse.status) {
                    resetValues()
                }
            } catch (e: ApiExceptions) {
                _onMessageError.postValue(e.message)
                _isViewLoading.postValue(false)
            } catch (e: NoInternetException) {
                _onMessageError.postValue(e.message)
                _isViewLoading.postValue(false)
            }
        }
        return _uploadFileLink!!
    }

    private fun uploadLInkParam(): JsonObject {
        val inputParam = JsonObject()
        inputParam.addProperty(
            Constant.REQUEST_MODE,
            Constant.REQUEST_ADD_TO_DRIVE_WITH_LINK
        )
        inputParam.addProperty(Constant.REUQEST_SHARE_ID, 0)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(
            Constant.REQUEST_USER_TYPE,
            prefUtils.getUserData()?.usertype
        )
        inputParam.addProperty(
            Constant.REQUEST_STUDENTID,
            prefUtils.getUserData()?.studentId
        )
        inputParam.addProperty(Constant.REQUEST_STANDARDID, standardid.value)
        inputParam.addProperty(Constant.REQUEST_FILE_TITLE, topic.value)
        inputParam.addProperty(Constant.REQUEST_FILE_DESCR, description.value)
        inputParam.addProperty(Constant.REQUEST_FILE_TYPE, "L")
        inputParam.addProperty(Constant.REQUEST_KW_TYPE, kwtype.value)
//        inputParam.addProperty(Constant.REQUEST_FILE_EXT, "")
//        inputParam.addProperty(Constant.REQUEST_FILE_SIZE, "")
        inputParam.addProperty(Constant.REQUEST_LINK_URL, linkurl.value)
        return inputParam
    }

    private fun resetValues() {
        topic.value = ""
        description.value = ""
        linkurl.value = ""
        file.value = null
    }

    fun callFileAddDrive(): LiveData<AssignmentSubmissionModel> {
        Coroutines.main {
            _isViewLoading.postValue(true)
            val fileReqBodyLicense = file.value?.asRequestBody("image/*".toMediaTypeOrNull())
            val userImageBody = MultipartBody.Part.createFormData(Constant.REQUEST_IMAGE, file.value?.name, fileReqBodyLicense!!)
            val shareid = "".toRequestBody("text/plain".toMediaTypeOrNull())
            val userid = prefUtils.getUserData()!!.userid?.toRequestBody("text/plain".toMediaTypeOrNull())
            val usertype = prefUtils.getUserData()!!.usertype?.toRequestBody("text/plain".toMediaTypeOrNull())
            val studentid = prefUtils.getUserData()!!.studentId?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filetitle = topic.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filedescr = description.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filetype = "F".toRequestBody("text/plain".toMediaTypeOrNull())
            val fileext = fileext.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            val filesize = filesize.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            val uploadtype = "D".toRequestBody("text/plain".toMediaTypeOrNull())
            val knowledgeType = kwtype.value?.toRequestBody("text/plain".toMediaTypeOrNull())
            val subjectId = subjectId.value?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val standardId = standardid.value?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            try {
                val response = repository.callFileAddDrive(
                    userImageBody,
                    shareid,
                    userid,
                    usertype,
                    studentid,
                    filetitle!!,
                    filedescr!!,
                    filetype,
                    fileext!!,
                    filesize!!,
                    uploadtype,
                    knowledgeType!!,
                    subjectId!!,
                    standardId!!)
                _upload_selected_file.postValue(response)
                _isViewLoading.postValue(false)
                if (response.status) {
                    resetValues()
                }
            } catch (e: ApiExceptions) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            } catch (e: NoInternetException) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(e.message)
            }
        }
        return _upload_selected_file!!
    }
}