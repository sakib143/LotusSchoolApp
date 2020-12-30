package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

data class ViewResultModel(
    @Expose
    @SerializedName("data")
    var `data`: List<Data>,
    @Expose
    @SerializedName("message")
    var message: String,
    @Expose
    @SerializedName("status")
    var status: Boolean
) {
    @Keep
    data class Data(
        @Expose
        @SerializedName("ClassName")
        var className: String,
        @Expose
        @SerializedName("description")
        var description: String,
        @Expose
        @SerializedName("DispEndTime")
        var dispEndTime: String,
        @Expose
        @SerializedName("duration")
        var duration: Double,
        @Expose
        @SerializedName("ExamEndDateTime")
        var examEndDateTime: String,
        @Expose
        @SerializedName("ExamStartDateTime")
        var examStartDateTime: String,
        @Expose
        @SerializedName("examdate")
        var examdate: String,
        @Expose
        @SerializedName("examid")
        var examid: String,
        @Expose
        @SerializedName("examname")
        var examname: String,
        @Expose
        @SerializedName("examno")
        var examno: String,
        @Expose
        @SerializedName("examtime")
        var examtime: String,
        @Expose
        @SerializedName("examtime1")
        var examtime1: String,
        @Expose
        @SerializedName("IsShowAttendButton")
        var isShowAttendButton: Int,
        @Expose
        @SerializedName("islive")
        var islive: Any,
        @Expose
        @SerializedName("ispublish")
        var ispublish: Boolean,
        @Expose
        @SerializedName("isrealtime")
        var isrealtime: Boolean,
        @Expose
        @SerializedName("StandardName")
        var standardName: String,
        @Expose
        @SerializedName("SubjectName1")
        var subjectName1: String,
        @Expose
        @SerializedName("subjectname")
        var subjectname: String,
        @Expose
        @SerializedName("subjid")
        var subjid: Int,
        @Expose
        @SerializedName("totalmarks")
        var totalmarks: Double,
        @Expose
        @SerializedName("totalobtainedmarks")
        var totalobtainedmarks: Double
    )
}