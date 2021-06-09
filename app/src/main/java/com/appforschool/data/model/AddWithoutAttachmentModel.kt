package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AddWithoutAttachmentModel(
    @Expose
    @SerializedName("data")
    var `data`: List<Data>,
    @Expose
    @SerializedName("message")
    var message: String,
    @Expose
    @SerializedName("status")
    var status: Boolean
) {
    @Keep
    data class Data(
        @Expose
        @SerializedName("message")
        var message: String,
        @Expose
        @SerializedName("status")
        var status: String
    )
}