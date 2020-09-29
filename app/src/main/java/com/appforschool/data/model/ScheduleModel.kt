package com.appforschool.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ScheduleModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
) {
    data class Data(
        @Expose
        @SerializedName("Column1") var column1: String = "",
        @SerializedName("CourseName") var courseName: String = "",
        @Expose
        @SerializedName("duration") var duration: Double = 0.0,
        @Expose
        @SerializedName("meetinglink") var meetinglink: String = "",
        @Expose
        @SerializedName("topic") var topic: String = "",
        @Expose
        @SerializedName("scheduledate") var scheduledate: Any = Any(),
        @Expose
        @SerializedName("scheduletime") var scheduletime: Any = Any(),
        @Expose
        @SerializedName("schid") var schid: Double = 0.0,
        @Expose
        @SerializedName("subjid") var subjid: Double = 0.0,
        @Expose
        @SerializedName("teacherid") var teacherid: Double = 0.0,
        @Expose
        @SerializedName("schdatetime") var schdatetime: String = ""



    )
}