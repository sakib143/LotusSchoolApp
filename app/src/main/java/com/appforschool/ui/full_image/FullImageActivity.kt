package com.appforschool.ui.full_image

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityFullImageBinding
import com.appforschool.databinding.ActivityFullImageBindingImpl
import com.appforschool.databinding.ActivityLoginBinding
import com.appforschool.ui.attendexam.AttendExamActivity
import com.appforschool.ui.auth.login.LoginViewModel
import com.appforschool.utils.Constant
import com.bumptech.glide.Glide
import javax.inject.Inject

class FullImageActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_full_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val ivFullImage = findViewById<View>(R.id.ivFullImage) as ImageView

        ivFullImage.transitionName = Constant.IMAGE_FULL_ZOOM_ANIM

        Glide.with(this@FullImageActivity)
            .load(intent.getStringExtra(Constant.REQUEST_LINK_URL))
            .centerCrop()
            .into(ivFullImage)

    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, imageUrl: String) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_LINK_URL, imageUrl)
    }
}