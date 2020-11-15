package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeApiModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "", // success
    @Expose
    @SerializedName("status") var status: Boolean = false // true
) {

    data class Data(
        @Expose
        @SerializedName("ishost") var ishost: Int = 0, // 0
        @Expose
        @SerializedName("standardid") var standardid: Int = 0, // 109.0
        @Expose
        @SerializedName("standardname") var standardname: String = "", // Class 2
        @Expose
        @SerializedName("StudentId") var studentId: Int = 0, // 52
        @Expose
        @SerializedName("studentname") var studentname: String = "", // Sunil Khanna
        @Expose
        @SerializedName("usertype") var usertype: String = "",
        @Expose
        @SerializedName("currentversion") var currentversion: String = "",
        @Expose
        @SerializedName("isforceupdate") var isforceupdate: String = "",
        @Expose
        @SerializedName("emailid") var emailid: String = "",
        @Expose
        @SerializedName("phone1") var phone1: String = "",
        @Expose
        @SerializedName("lastname") var lastname: String = "",
        @Expose
        @SerializedName("firstname") var firstname: String = "",
        @Expose
        @SerializedName("ProfileImage") var ProfileImage: String = "",
        @Expose
        @SerializedName("isopenprofile") var isopenprofile: Int = 0
    )
}