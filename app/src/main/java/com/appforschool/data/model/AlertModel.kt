package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep


data class AlertModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String? = "", // success
    @Expose
    @SerializedName("status") var status: Boolean? = false // true
) {
    data class Data(
        @Expose
        @SerializedName("NotificationClass") var notificationClass: String? = "", // warning
        @Expose
        @SerializedName("NotificationDate") var notificationDate: String? = "", // 2019-09-13T23:02:42.337
        @Expose
        @SerializedName("NotificationDesc") var notificationDesc: String? = "", // Exam will be on This Monday
        @Expose
        @SerializedName("NotificationIcon") var notificationIcon: String? = "", // fa-hand-paper-o
        @Expose
        @SerializedName("NotificationType") var notificationType: String? = "", // Individual
        @Expose
        @SerializedName("NotificationViewed") var notificationViewed: String? = "" // Y
    )
}