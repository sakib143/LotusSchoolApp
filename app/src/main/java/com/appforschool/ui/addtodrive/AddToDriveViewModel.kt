package com.appforschool.ui.addtodrive

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.StandardListModel
import com.appforschool.data.model.SubjectListModel
import com.appforschool.data.repository.AddToDriveRepository
import com.appforschool.utils.GlobalMethods
import com.appforschool.utils.LogM
import javax.inject.Inject


class AddToDriveViewModel @Inject constructor(
    private val application: MyApp,
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

    //Standard observer
    private val _subject: MutableLiveData<SubjectListModel> =
        MutableLiveData<SubjectListModel>()
    val subject: LiveData<SubjectListModel>
        get() = _subject


    var topic = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var directLinkUrl = MutableLiveData<String>()

    private val _isFileSelected = MutableLiveData<Boolean>()
    val isFileSelected: LiveData<Boolean> get() = _isFileSelected

    var alKnowledge: ArrayList<String> = repository.knowledgeTypeList()

    fun setFileSelect(isSelected: Boolean) {
        _isFileSelected.value = isSelected
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