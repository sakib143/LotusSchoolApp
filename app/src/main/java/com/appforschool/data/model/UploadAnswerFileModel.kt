package com.appforschool.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadAnswerFileModel (
    @Expose
    @SerializedName("message") var message: String = "", // File uploaded successfully
    @Expose
    @SerializedName("status") var status: Boolean = false // true
)