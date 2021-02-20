package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class SubjectListModel(
    @Expose
    @SerializedName("data") var `data`: ArrayList<Data> = arrayListOf(),
    @Expose
    @SerializedName("message") var message: String= "", // success
    @Expose
    @SerializedName("status") var status: Boolean = false // true
) {
    data class Data(
        @Expose
        @SerializedName("courseid") var courseid: Int = 0, // 714.0
        @Expose
        @SerializedName("coursename") var coursename: String = "" // Hindi
    )
}