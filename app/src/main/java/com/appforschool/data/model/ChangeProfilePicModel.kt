package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ChangeProfilePicModel(
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
)