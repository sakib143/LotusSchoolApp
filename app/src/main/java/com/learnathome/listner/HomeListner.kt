package com.learnathome.listner

import com.learnathome.data.model.ScheduleModel

interface HomeListner {
    fun openVideoCalling(scheduleModel: ScheduleModel.Data)
}