package com.appforschool.listner

import com.appforschool.data.model.AssignmentModel

interface AttendExamListner {
    fun attendExamClick(model: AssignmentModel.Data)
}