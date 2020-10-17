package com.appforschool.listner

import com.appforschool.data.model.*

interface HomeListner {
    fun openVideoCalling(scheduleModel: ScheduleModel.Data)
    fun openSubjectFile(model: SubjectDetailsModel.Data)
    fun openAssignmentFile(model: AssignmentModel.Data)
    fun openAlertDetails(model: AlertModel.Data)
    fun openExamDetails(model: ExamModel.Data)
    fun openDriveList(model: DriveModel.Data)
    fun shareDriveData(model: DriveModel.Data)
    fun openSubjectDetails(model: ScheduleModel.Data)
    fun openSubmissionFile(model: AssignmentModel.Data)
}