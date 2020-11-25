package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class StartExamModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
) {

    data class Data(
        @Expose
        @SerializedName("ClassName") var className: String = "",
        @Expose
        @SerializedName("description") var description: String = "",
        @Expose
        @SerializedName("DispEndTime") var dispEndTime: String = "",
        @Expose
        @SerializedName("duration") var duration: Int = 0,
        @Expose
        @SerializedName("ExamEndDateTime") var examEndDateTime: String = "",
        @Expose
        @SerializedName("ExamStartDateTime") var examStartDateTime: String = "",
        @Expose
        @SerializedName("examdate") var examdate: String = "",
        @Expose
        @SerializedName("examid") var examid: String = "",
        @Expose
        @SerializedName("examname") var examname: String = "",
        @Expose
        @SerializedName("examno") var examno: String = "",
        @Expose
        @SerializedName("examtime") var examtime: String = "",
        @Expose
        @SerializedName("examtime1") var examtime1: String = "",
        @Expose
        @SerializedName("IsShowAttendButton") var isShowAttendButton: Int = 0,
        @Expose
        @SerializedName("islive") var islive: Any = Any(),
        @Expose
        @SerializedName("ispublish") var ispublish: Boolean = false,
        @Expose
        @SerializedName("isrealtime") var isrealtime: Boolean = false,
        @Expose
        @SerializedName("StandardName") var standardName: String = "",
        @Expose
        @SerializedName("SubjectName1") var subjectName1: String = "",
        @Expose
        @SerializedName("subjectname") var subjectname: String = "",
        @Expose
        @SerializedName("subjid") var subjid: Int = 0,
        @Expose
        @SerializedName("totalmarks") var totalmarks: Double = 0.0
    )
}