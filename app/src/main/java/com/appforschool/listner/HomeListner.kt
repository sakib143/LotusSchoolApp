package com.appforschool.listner

import com.appforschool.data.model.ScheduleModel

interface HomeListner {
    fun openVideoCalling(scheduleModel: ScheduleModel.Data)
}