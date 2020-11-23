package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class ExamModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String? = "", // success
    @Expose
    @SerializedName("status") var status: Boolean? = false // true
) {
    data class Data(
        @Expose
        @SerializedName("description") var description: String? = "", // Descr
        @Expose
        @SerializedName("duration") var duration: Int? = 0, // 120.0
        @Expose
        @SerializedName("examdate") var examdate: String? = "", // 15-10-20
        @Expose
        @SerializedName("examname") var examname: String? = "", // Lesson
        @Expose
        @SerializedName("examno") var examno: String? = "", // MAT-001
        @Expose
        @SerializedName("examtime") var examtime: String? = "", // 10:00AM
        @Expose
        @SerializedName("islive") var islive: Boolean? = false, // false
        @Expose
        @SerializedName("ispublish") var ispublish: Boolean? = false, // false
        @Expose
        @SerializedName("isrealtime") var isrealtime: Boolean? = false, // false
        @Expose
        @SerializedName("subjectname") var subjectname: String? = "", // Maths
        @Expose
        @SerializedName("subjid") var subjid: Int? = 0, // 296
        @Expose
        @SerializedName("totalmarks") var totalmarks: Int = 0,
        @Expose
        @SerializedName("StandardName") var StandardName: String = "",
        @Expose
        @SerializedName("examtime1") var examtime1: String = "",

        )
}