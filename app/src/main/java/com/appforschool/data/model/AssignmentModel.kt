package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class AssignmentModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String? = "", // success
    @Expose
    @SerializedName("status") var status: Boolean? = false // true
) {
    data class Data(
        @Expose
        @SerializedName("filedescr") var filedescr: String? = "", // Assignment Maths - I Ch-1 Real Nos.
        @Expose
        @SerializedName("fileext") var fileext: String? = "", // .jpg
        @Expose
        @SerializedName("filetitle") var filetitle: String? = "", // Assignment Maths - I
        @Expose
        @SerializedName("filetype") var filetype: String? = "", // Text Book
        @Expose
        @SerializedName("linkurl") var linkurl: String? = "", // null
        @Expose
        @SerializedName("sharedby") var sharedby: String? = "", // Gayatri Mam
        @Expose
        @SerializedName("sharetime") var sharetime: String? = "", // 2020-07-10T18:25:10.31
        @Expose
        @SerializedName("sizeinkb") var sizeinkb: String? = "", // 107
        @Expose
        @SerializedName("subjectname") var subjectname: String? = "", // Maths
        @Expose
        @SerializedName("userid") var userid: Int? = 0,
        @Expose
        @SerializedName("assgdate") var assgdate: String? = "",
        @Expose
        @SerializedName("assgtime") var assgtime: String? = "",
        @Expose
        @SerializedName("filepath") var filepath: String? = "")
}