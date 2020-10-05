package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
) {
    data class Data(
        @Expose
        @SerializedName("StudentId") var studentId: String = "",
        @Expose
        @SerializedName("studentname") var studentname: String = "",
        @Expose
        @SerializedName("standardname") var standardname: String = "",
        @Expose
        @SerializedName("ishost") var ishost: Int = 0,
        @Expose
        @SerializedName("usertype") var usertype: String = "",
        @Expose
        @SerializedName("userid") var userid: String = "",
        @Expose
        @SerializedName("standardid") var standardid: Int = 0
        )

    /*
    usertype will be
    M for teacher (M = Mentor)
    S for Student
    A for Admin
     */

}