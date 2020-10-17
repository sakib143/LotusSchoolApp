package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class AssignmentSubmissionModel(
    @Expose
    @SerializedName("data") var `data`: Any = Any(), // null
    @Expose
    @SerializedName("message") var message: String = "", // File uploaded successfully
    @Expose
    @SerializedName("status") var status: Boolean = false // true
)