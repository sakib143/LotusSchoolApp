package com.appforschool.ui.full_image

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.ui.attendexam.AttendExamActivity
import com.appforschool.utils.Constant
import kotlinx.android.synthetic.main.activity_full_exam_date.*

class FullExamDateActivity  : BaseActivity()  {

    override fun layoutId() = R.layout.activity_full_exam_date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        rvFIA.transitionName = Constant.IMAGE_FULL_ZOOM_ANIM


        tvDateFIA.setText(intent.getStringExtra(Constant.REQUEST_MODE_START_EXAM))
        tvTimeFIA.setText(intent.getStringExtra(Constant.REQUEST_MODE_END_EXAM))


    }

}