package com.appforschool.utils.databindingadapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.appforschool.R
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.DriveModel
import com.appforschool.data.model.ExamModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.utils.Constant
import com.appforschool.utils.OnDragTouchListener
import com.appforschool.utils.circle_imageview.CircularImageView
import com.appforschool.utils.hide
import com.appforschool.utils.show
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("imagFromAsset")
fun setImageFromAsset(view: ImageView, fileName: String) {
    try {
        val bitmap = BitmapFactory.decodeStream(view.context?.assets?.open(fileName))
        view.setImageBitmap(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@BindingAdapter("setSubjectIcons")
fun setSubjectIcons(view: ImageView, model: SubjectDetailsModel.Data) {
    if (model.fileext.equals(".pdf", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_pdf)
    } else if(model.fileext.equals(".mp4", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_video_play)
    }else if(model.fileext.equals(".ppt", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_ppt)
    }else if(model.fileext.equals(".xls", ignoreCase = true) || model.fileext.equals(
            ".xlsx",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_excel)
    }else if(model.fileext.equals(".jpg", ignoreCase = true) || model.fileext.equals(
            ".gif",
            ignoreCase = true
        ) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
            ".tiff",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_image_file)
    }else if(model.fileext.equals(".doc", ignoreCase = true) || model.fileext.equals(
            ".docx",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_doc)
    }else  {
        view.setImageResource(R.drawable.ic_file_for_assignment)
    }
}

@BindingAdapter("setAssignmentIcon")
fun setAssignmentIcon(view: ImageView, model: AssignmentModel.Data) {
    if (model.fileext.equals(".pdf", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_pdf)
    } else if(model.fileext.equals(".mp4", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_video_play)
    }else if(model.fileext.equals(".ppt", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_ppt)
    }else if(model.fileext.equals(".xls", ignoreCase = true) || model.fileext.equals(
            ".xlsx",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_excel)
    }else if(model.fileext.equals(".jpg", ignoreCase = true) || model.fileext.equals(
            ".gif",
            ignoreCase = true
        ) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
            ".tiff",
            ignoreCase = true
        ) || model.fileext.equals(".jpeg", ignoreCase = true) ) {
        view.setImageResource(R.drawable.ic_image_file)
    }else if(model.fileext.equals(".doc", ignoreCase = true) || model.fileext.equals(
            ".docx",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_doc)
    }else  {
        view.setImageResource(R.drawable.ic_file_for_assignment)
    }
}

@BindingAdapter("setDriveListIcon")
fun setDriveListIcon(view: ImageView, model: DriveModel.Data) {
    if (model.fileext.equals(".pdf", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_pdf)
    } else if(model.fileext.equals(".mp4", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_video_play)
    }else if(model.fileext.equals(".ppt", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_ppt)
    }else if(model.fileext.equals(".xls", ignoreCase = true) || model.fileext.equals(
            ".xlsx",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_excel)
    }else if(model.fileext.equals(".jpg", ignoreCase = true) || model.fileext.equals(
            ".gif",
            ignoreCase = true
        ) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(
            ".tiff",
            ignoreCase = true
        ) || model.fileext.equals(".jpeg", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_image_file)
    }else if(model.fileext.equals(".doc", ignoreCase = true) || model.fileext.equals(
            ".docx",
            ignoreCase = true
        )) {
        view.setImageResource(R.drawable.ic_doc)
    }else  {
        view.setImageResource(R.drawable.ic_file_for_assignment)
    }
}

@BindingAdapter("imageZoomUsingURL", requireAll = false)
fun imageZoomUsingURL(view: ImageView, url: String) {
    view.transitionName = Constant.IMAGE_FULL_ZOOM_ANIM
    Glide.with(view.context)
        .load(url)
        .centerCrop()
        .into(view)
}

@BindingAdapter("ImageUrl", "placeholderImage", "errorImage", requireAll = false)
fun loadImageFromUrl(view: ImageView, url: String?, isPlacehoder: Boolean, isError: Boolean) {
    val requestOption: RequestOptions = RequestOptions()
    if (isPlacehoder) {
        requestOption.placeholder(R.mipmap.ic_launcher)
    }
    if (isError) {
        requestOption.placeholder(R.mipmap.ic_launcher)
    }
    Glide.with(view.context)
        .load(url)
        .into(view)
}

@BindingAdapter("LoadCircularImage", "placeholderImage", "errorImage", requireAll = false)
fun loadCircularImage(
    view: CircularImageView,
    url: String?,
    isPlacehoder: Boolean,
    isError: Boolean
) {
    val requestOption: RequestOptions = RequestOptions()
    if (isPlacehoder) {
        requestOption.placeholder(R.drawable.profile_icons)
    }
    if (isError) {
        requestOption.placeholder(R.drawable.profile_icons)
    }
    Glide.with(view.context)
        .load(url)
        .into(view)
}

@BindingAdapter("webviewUrl")
fun WebView.setUrl(url: String) {
    this.loadUrl(url)
}

@BindingAdapter("hideShowPassword")
fun setHideShowPassword(editext: EditText, isShow: Boolean) {
    if (isShow) {
        editext.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
    } else {
        editext.setTransformationMethod(PasswordTransformationMethod.getInstance())
    }
}

@BindingAdapter("setTodayExam")
fun setTodayExam(attentExam: TextView, examModel: ExamModel.Data) {
    val currentTime = Calendar.getInstance()
    val curretDateFormat = SimpleDateFormat(Constant.DATE_FORMAT_YY_MM_DD)
    val strCurrentDate =  curretDateFormat.format(currentTime.time)

    val dateTO = curretDateFormat.parse(examModel.ExamStartDateTime)
    val calendarTo = Calendar.getInstance()
    calendarTo!!.time = dateTO
    val strExamTime =  curretDateFormat.format(calendarTo.time)

    if(strCurrentDate.equals(strExamTime, ignoreCase = true)){
        attentExam.setText("Today")
        attentExam.setText(R.string.today)
        attentExam.setTextColor(Color.parseColor("#FF0000"))
    }
}

@BindingAdapter("setTodayTime")
fun setTodayTime(attentExam: TextView, examModel: ExamModel.Data) {
    val currentTime = Calendar.getInstance()
    val curretDateFormat = SimpleDateFormat(Constant.DATE_FORMAT_YY_MM_DD)
    val strCurrentDate =  curretDateFormat.format(currentTime.time)

    val dateTO = curretDateFormat.parse(examModel.ExamStartDateTime)
    val calendarTo = Calendar.getInstance()
    calendarTo!!.time = dateTO
    val strExamTime =  curretDateFormat.format(calendarTo.time)

    if(strCurrentDate.equals(strExamTime, ignoreCase = true)){
        attentExam.setTextColor(Color.parseColor("#FF0000"))
    }
}

@BindingAdapter("setAttentAndResult")
fun setAttentAndResult(textview: TextView, examModel: ExamModel.Data) {
    if (examModel.isshowviewresult == 1) {
        textview.setText(R.string.view_result)
        textview.show()
    } else if (examModel.IsShowAttendButton == 1) {
        textview.setText(R.string.attend)
        textview.show()
    } else {
        textview.hide()
    }
}

@BindingAdapter("setTotakAndObtainMarks")
fun setTotakAndObtainMarks(textview: TextView, examModel: ExamModel.Data) {
    if (examModel.isshowviewresult == 1) {
        val obtainMarks: String = "Marks: "  + examModel.ObtainedMarks + "/"+examModel.totalmarks.toString()
        textview.setText(obtainMarks)
    } else {
        textview.setText("Marks: " + examModel.totalmarks)
    }
}

@BindingAdapter("setTouchListner")
fun setTouchListner(textview: TextView, value: Boolean) {
    textview.setOnTouchListener(OnDragTouchListener(textview))
}


//@BindingAdapter("setViewmoreNotification")
//fun setViewmoreNotification(textview: TextView) {
//    textview.post(Runnable {
//        LogM.e("=> text lenght " + textview.lineCount)
//    })
//}


//@BindingAdapter("setTextviewLineCount")
//fun setTextviewLineCount(textview: TextView, count: Int) {
//    textview.maxLines = 5
//}


//@BindingAdapter("setAttentExam")
//fun setAttentExam(attentExam: TextView, examModel: ExamModel.Data) {
////    val strDate = examModel.examdate + " " + examModel.examtime1
//    LogM.e("Full date is $examModel.ExamStartDateTime")
//    val currentTime = Calendar.getInstance()
//    val df = SimpleDateFormat(Constant.DATE_FORMAT_ONE)
//    val strCurrentTime: String = df.format(currentTime!!.getTime())
//    LogM.e("Current time is $strCurrentTime")
//
//    val SDF_FROM_DATE = SimpleDateFormat(Constant.DATE_FORMAT_ONE)
//
//    val dateFROM = SDF_FROM_DATE.parse(examModel.ExamStartDateTime)
//    val calendarFrom = Calendar.getInstance()
//    calendarFrom!!.time = dateFROM
//
//    val dateTO = SDF_FROM_DATE.parse(examModel.ExamEndDateTime)
//    val calendarTo = Calendar.getInstance()
//    calendarTo!!.time = dateTO
//
//    if (currentTime!!.after(calendarFrom) && currentTime!!.before(calendarTo)) {
//        attentExam.show()
//    } else {
//        attentExam.hide()
//    }
//}

//private fun toTime(examTime: String,duration: Int): Calendar? {
//    val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT_ONE)
//    val date = dateFormat.parse(examTime)
//    val calendar = Calendar.getInstance()
//    calendar!!.time = date
//    calendar!!.add(Calendar.MINUTE, duration)
//    return calendar
//}
//
//private fun fromTime(examTime: String): Calendar? {
//    val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT_ONE)
//    var myDate = dateFormat.parse(examTime)
//    val calendar = Calendar.getInstance()
//    calendar.time = myDate
////        val formattedDate: String = dateFormat.format(calendar!!.getTime())
////        Log.e("=>","From time " + formattedDate)
//    return calendar
//}
