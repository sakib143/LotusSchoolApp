package com.appforschool.ui.full_image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.allenxuan.xuanyihuang.xuanimageview.XuanImageView
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.ui.attendexam.AttendExamActivity
import com.appforschool.utils.Constant
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_full_image.*

class FullImageActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_full_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val ivFullImage = findViewById<View>(R.id.ivFullImage) as XuanImageView

        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_loading_buffering)
        requestOptions.error(R.drawable.ic_error)

        Glide.with(this@FullImageActivity)
            .setDefaultRequestOptions(requestOptions)
            .load(intent.getStringExtra(Constant.REQUEST_LINK_URL))
            .into(ivFullImage)
    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, imageUrl: String) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_LINK_URL, imageUrl)
    }
}