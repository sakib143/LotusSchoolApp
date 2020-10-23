package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SubjectModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
) {
    data class Data(
        @Expose
        @SerializedName("courseid") var courseid: Int = 0,
        @Expose
        @SerializedName("coursename") var coursename: String = "",
        @Expose
        @SerializedName("isselective") var isselective: Boolean = false,
        @Expose
        @SerializedName("topic") var topic: String = "",
        @Expose
        @SerializedName("standardname") var standardname: String = "")
}