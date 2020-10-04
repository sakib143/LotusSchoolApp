package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class SubjectDetailsModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String? = "", // success
    @Expose
    @SerializedName("status") var status: Boolean? = false // true
) {

    data class Data(
        @Expose
        @SerializedName("FileDescr") var fileDescr: String? = "", // Please use this PPT in conjunction with the Student Guide
        @Expose
        @SerializedName("fileext") var fileext: String? = "", // .ppt
        @Expose
        @SerializedName("filepath") var filepath: String? = "", // /Drive/Course-File/1_03_21_20_11_46_36_s2101_Problem Solving_PowerPoint Slides.ppt
        @Expose
        @SerializedName("filetitle") var filetitle: String? = "", // Problem Solving Slides
        @Expose
        @SerializedName("filetype") var filetype: String? = "", // null
        @Expose
        @SerializedName("IsDemoFile") var isDemoFile: Boolean? = false, // false
        @Expose
        @SerializedName("sizeinkb") var sizeinkb: String? = "",
        @Expose
        @SerializedName("Column1")
        var Column1: String? = "",
        @Expose
        @SerializedName("Column2")
        var Column2: String? = "" // 4246

    )
}