package com.appforschool.ui.addtodrive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.StandardListModel
import com.appforschool.data.model.SubjectListModel
import com.appforschool.data.model.UploadFileUrlModel
import com.appforschool.databinding.ActivityAddToDriveBinding
import com.appforschool.ui.addtodrive.adapter.KnowledgeSpinnerAdapter
import com.appforschool.ui.addtodrive.adapter.StandardAdapter
import com.appforschool.ui.addtodrive.adapter.SubjectAdapter
import com.appforschool.utils.toast
import kotlinx.android.synthetic.main.activity_add_to_drive.*
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
}