package com.appforschool.ui.addtodrive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityAddToDriveBinding
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

        viewModel.setFileSelect(true)

        val adapter = KnowledgeSpinnerAdapter(this@AddToDriveActivity, viewModel.alKnowledge)
        spinnerKnowledgeList.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, AddToDriveActivity::class.java)

    }

    fun closeScreen() {
        finish()
    }
}