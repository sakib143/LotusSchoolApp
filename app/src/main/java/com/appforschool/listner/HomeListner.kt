package com.appforschool.listner

import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.ScheduleModel
import com.appforschool.data.model.SubjectDetailsModel

interface HomeListner {
    fun openVideoCalling(scheduleModel: ScheduleModel.Data)
    fun openSubjectFile(model: SubjectDetailsModel.Data)
    fun openAssignmentFile(model: AssignmentModel.Data)

}