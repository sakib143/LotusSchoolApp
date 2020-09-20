package com.learnathome.data.model


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
        @SerializedName("groupid") var groupid: Int = 0,
        @Expose
        @SerializedName("meetinglink") var meetinglink: String = "",
        @Expose
        @SerializedName("scheduledate") var scheduledate: String = "",
        @Expose
        @SerializedName("scheduletime") var scheduletime: String = "",
        @Expose
        @SerializedName("schid") var schid: Double = 0.0,
        @Expose
        @SerializedName("subjid") var subjid: Double = 0.0,
        @Expose
        @SerializedName("teacherid") var teacherid: Double = 0.0
    )
}