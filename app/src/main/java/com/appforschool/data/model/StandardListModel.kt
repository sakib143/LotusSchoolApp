package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class StandardListModel(
    @Expose
    @SerializedName("data") var `data`: ArrayList<Data> = arrayListOf(),
    @Expose
    @SerializedName("message") var message: String = "", // success
    @Expose
    @SerializedName("status") var status: Boolean = false // true
) {
    data class Data(
        @Expose
        @SerializedName("groupid") var groupid: Int? = 0, // 88
        @Expose
        @SerializedName("groupname") var groupname: String? = "" // Class1
    )
}