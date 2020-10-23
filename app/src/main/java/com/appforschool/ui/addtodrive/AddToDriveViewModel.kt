package com.appforschool.ui.addtodrive

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.api.ApiExceptions
import com.appforschool.api.NoInternetException
import com.appforschool.data.model.GetVersionModel
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.StandardListModel
import com.appforschool.data.model.SubjectListModel
import com.appforschool.data.repository.AddToDriveRepository
import com.appforschool.utils.*
import com.google.gson.JsonObject
import javax.inject.Inject


class AddToDriveViewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
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


    var topic = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var directLinkUrl = MutableLiveData<String>()

    private val _isFileSelected = MutableLiveData<Boolean>()
    val isFileSelected: LiveData<Boolean> get() = _isFileSelected

    var alKnowledge: ArrayList<String> = repository.knowledgeTypeList()

    fun setFileSelect(isSelected: Boolean) {
        _isFileSelected.value = isSelected
    }

    fun checkUserType() {
        if (prefUtils.getUserData()?.usertype.equals("S", ignoreCase = true)) {
            _setStandardVisiblity.value = false
            _setSubjectVisiblity.value = true
            executerSubjectList(prefUtils.getUserData()?.studentId!!)
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

    }

}