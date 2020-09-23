package com.appforschool.ui.commonwebview

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityCommonWebviewBinding
import com.appforschool.utils.Constant
import javax.inject.Inject

class CommonWebviewActivity : BaseBindingActivity<ActivityCommonWebviewBinding>() {

    @Inject
    lateinit var viewModel: CommonWebviewViewModel

    override fun layoutId() = R.layout.activity_common_webview

    override fun initializeBinding(binding: ActivityCommonWebviewBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_webview)

        viewModel.webviewUrl.value =  intent.getStringExtra(Constant.WEBVIEW_URL)
    }


    companion object {
        @JvmStatic
        fun intentFor(context: Context, videoUrls : String) =
            Intent(context, CommonWebviewActivity::class.java)
                .putExtra(Constant.WEBVIEW_URL, videoUrls)
    }


}