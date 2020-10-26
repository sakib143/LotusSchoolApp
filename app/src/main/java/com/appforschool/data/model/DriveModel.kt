package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class DriveModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String? = "", // success
    @Expose
    @SerializedName("status") var status: Boolean? = false // true
) {
    data class Data(
        @Expose
        @SerializedName("filedescr") var filedescr: String? = "", // To solve first 5 Questions and write summary of method used
        @Expose
        @SerializedName("fileext") var fileext: String? = "", // .pdf
        @Expose
        @SerializedName("filepath") var filepath: String? = "", // https://mywhiteboardlabs.com//Drive/Students/188_10_05_20_10_06_40_Math Assgn1 C2.pdf
        @Expose
        @SerializedName("filetitle") var filetitle: String? = "", // Math Ch-1
        @Expose
        @SerializedName("filetype") var filetype: String? = "", // Assignment
        @Expose
        @SerializedName("linkurl") var linkurl: String? = "", // null
        @Expose
        @SerializedName("sizeinkb") var sizeinkb: String? = "", // 327KB
        @Expose
        @SerializedName("subjectname") var subjectname: String? = "", // Maths
        @Expose
        @SerializedName("userid") var userid: Int? = 0,
        @Expose
        @SerializedName("filedate") var filedate: String? = "", // Maths
        @Expose
        @SerializedName("filetime") var filetime: String? = "",
        @Expose
        @SerializedName("shareid") var shareid: String = ""


    )
}