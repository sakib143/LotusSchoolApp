package com.appforschool.utils.databindingadapter

import android.graphics.BitmapFactory
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.appforschool.R
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.DriveModel
import com.appforschool.data.model.ExamModel
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import com.appforschool.utils.hide
import com.appforschool.utils.show
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
    }else if(model.fileext.equals(".xls", ignoreCase = true) || model.fileext.equals(".xlsx", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_excel)
    }else if(model.fileext.equals(".jpg", ignoreCase = true) || model.fileext.equals(".gif", ignoreCase = true) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(".tiff", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_image_file)
    }else if(model.fileext.equals(".doc", ignoreCase = true) || model.fileext.equals(".docx", ignoreCase = true)) {
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
    }else if(model.fileext.equals(".xls", ignoreCase = true) || model.fileext.equals(".xlsx", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_excel)
    }else if(model.fileext.equals(".jpg", ignoreCase = true) || model.fileext.equals(".gif", ignoreCase = true) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(".tiff", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_image_file)
    }else if(model.fileext.equals(".doc", ignoreCase = true) || model.fileext.equals(".docx", ignoreCase = true)) {
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
    }else if(model.fileext.equals(".xls", ignoreCase = true) || model.fileext.equals(".xlsx", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_excel)
    }else if(model.fileext.equals(".jpg", ignoreCase = true) || model.fileext.equals(".gif", ignoreCase = true) || model.fileext.equals(".png", ignoreCase = true) || model.fileext.equals(".tiff", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_image_file)
    }else if(model.fileext.equals(".doc", ignoreCase = true) || model.fileext.equals(".docx", ignoreCase = true)) {
        view.setImageResource(R.drawable.ic_doc)
    }else  {
        view.setImageResource(R.drawable.ic_file_for_assignment)
    }
}



@BindingAdapter("imagUrl", "placeholderImage", "errorImage", requireAll = false)
fun loadImageFromUrl(view: ImageView, url: String, isPlacehoder: Boolean, isError: Boolean) {
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

@BindingAdapter("setAttentExam")
fun setAttentExam(attentExam: TextView, examModel: ExamModel.Data) {
    val strDate = examModel.examdate + " " + examModel.examtime1
    LogM.e("Full date is $strDate")
    val currentTime = Calendar.getInstance()
    val df = SimpleDateFormat(Constant.DATE_FORMAT)
    val strCurrentTime: String = df.format(currentTime!!.getTime())
    LogM.e("Current time is $strCurrentTime")
    if (currentTime!!.after(fromTime(strDate,examModel.duration!!)) && currentTime!!.before(toTime(strDate,examModel.duration!!))) {
        attentExam.show()
    }else {
        attentExam.hide()
    }
}

private fun toTime(examTime: String,duration: Int): Calendar? {
    val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
    val date = dateFormat.parse(examTime)
    val calendar = Calendar.getInstance()
    calendar!!.time = date
    calendar!!.add(Calendar.MINUTE, duration)
    return calendar
}

private fun fromTime(examTime: String,duration: Int): Calendar? {
    val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
    var myDate = dateFormat.parse(examTime)
    val calendar = Calendar.getInstance()
    calendar.time = myDate
//        val formattedDate: String = dateFormat.format(calendar!!.getTime())
//        Log.e("=>","From time " + formattedDate)
    return calendar
}
