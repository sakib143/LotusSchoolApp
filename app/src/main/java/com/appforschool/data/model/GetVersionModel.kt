package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class GetVersionModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "", // success
    @Expose
    @SerializedName("status") var status: Boolean = false // true
) {
    data class Data(
        @Expose
        @SerializedName("CurrentVersion") var currentVersion: String? = "", // 1.1
        @Expose
        @SerializedName("IsForceUpdate") var isForceUpdate: String? = "" // No
    )
}