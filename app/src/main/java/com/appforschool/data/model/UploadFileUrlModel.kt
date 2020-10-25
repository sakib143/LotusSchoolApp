package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class UploadFileUrlModel(
    @Expose
    @SerializedName("data") var `data`: Any = Any(),
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
)