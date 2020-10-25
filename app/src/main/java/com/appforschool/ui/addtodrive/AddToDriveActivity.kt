package com.appforschool.ui.addtodrive

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.StandardListModel
import com.appforschool.data.model.SubjectListModel
import com.appforschool.data.model.UploadFileUrlModel
import com.appforschool.databinding.ActivityAddToDriveBinding
import com.appforschool.ui.addtodrive.adapter.KnowledgeSpinnerAdapter
import com.appforschool.ui.addtodrive.adapter.StandardAdapter
import com.appforschool.ui.addtodrive.adapter.SubjectAdapter
import com.appforschool.utils.ImageFilePath
import com.appforschool.utils.toast
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_add_to_drive.*
import java.io.File
import javax.inject.Inject

class AddToDriveActivity : BaseBindingActivity<ActivityAddToDriveBinding>() {

    override fun layoutId() = R.layout.activity_add_to_drive

    @Inject
    lateinit var viewModel: AddToDriveViewModel

    override fun initializeBinding(binding: ActivityAddToDriveBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()
        viewModel.setFileSelect(true)
        viewModel.checkUserType()
        setKnowledgeSpinner()

    }

    private fun setKnowledgeSpinner() {
        val adapter = KnowledgeSpinnerAdapter(this@AddToDriveActivity, viewModel.alKnowledge)
        spKnowledgeAD.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, AddToDriveActivity::class.java)

    }

    private fun setObserver() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.standard.observe(this, standardObserver)
        viewModel.subject.observe(this, subjectObserver)
        viewModel.uploadFileLink.observe(this, uploadFileLinkObserver)
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val standardObserver = Observer<StandardListModel> {
        if (it.status) {
            setStandardSpinner(it)
        } else {
            toast(it!!.message)
        }
    }

    private val uploadFileLinkObserver = Observer<UploadFileUrlModel> {
        if (it.status) {
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }

    private fun setStandardSpinner(standardListModel: StandardListModel) {
        val adapter = StandardAdapter(this@AddToDriveActivity, standardListModel.data)
        spStandardAD.adapter = adapter
    }

    private val subjectObserver = Observer<SubjectListModel> {
        if (it.status) {
            setSubjectAdapter(it)
        } else {
            toast(it!!.message)
        }
    }

    private fun setSubjectAdapter(it: SubjectListModel) {
        val adapter = SubjectAdapter(this@AddToDriveActivity, it.data)
        spSubjectAD.adapter = adapter
    }

    fun closeScreen() {
        finish()
    }

    fun chooseFile(){
        checkFileSubmitPermission()
    }

    fun checkFileSubmitPermission() = runWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE) {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.setType("*/*")
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKFILE_RESULT_CODE -> {
                val filePath = ImageFilePath.getPath(this@AddToDriveActivity, data?.data)
                val file: File = File(filePath)
                viewModel.file.value = file
                val fileSizeInBytes = file.length()
                val fileSizeInKB = fileSizeInBytes/ 1024
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString())
                viewModel.filesize.value = fileSizeInKB.toString()
                viewModel.fileext.value = fileExtension
            }
        }
    }

}