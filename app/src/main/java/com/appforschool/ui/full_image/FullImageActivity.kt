package com.appforschool.ui.full_image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import kotlin.math.max
import kotlin.math.min
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.ui.attendexam.AttendExamActivity
import com.appforschool.utils.Constant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_full_image.*

class FullImageActivity : BaseActivity() {

    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    override fun layoutId() = R.layout.activity_full_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val ivFullImage = findViewById<View>(R.id.ivFullImage) as ImageView

        ivFullImage.transitionName = Constant.IMAGE_FULL_ZOOM_ANIM

        Glide.with(this@FullImageActivity)
            .load(intent.getStringExtra(Constant.REQUEST_LINK_URL))
            .into(ivFullImage)

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, imageUrl: String) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_LINK_URL, imageUrl)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }
    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            scaleFactor *= scaleGestureDetector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 10.0f))
            ivFullImage.scaleX = scaleFactor
            ivFullImage.scaleY = scaleFactor
            return true
        }
    }
}