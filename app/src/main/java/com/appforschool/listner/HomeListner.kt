package com.appforschool.listner

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.appforschool.data.model.*

interface HomeListner {
    fun openVideoCalling(scheduleModel: ScheduleModel.Data)
    fun openSubjectFile(view: ImageView,model: SubjectDetailsModel.Data)
    fun openAssignmentFile(view: ImageView, model: AssignmentModel.Data)
    fun openAlertDetails(model: AlertModel.Data)
    fun openExamDetails(model: ExamModel.Data)
    fun openDriveList(imageView: ImageView,model: DriveModel.Data)
    fun shareDriveData(model: DriveModel.Data)
    fun openSubjectDetails(model: ScheduleModel.Data)
    fun openSubmissionFile(model: AssignmentModel.Data)
    fun openExamDetailsZoom(view: LinearLayout, date:String, time:String)
}