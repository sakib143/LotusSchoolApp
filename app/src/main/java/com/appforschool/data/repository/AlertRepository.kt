package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.LoginModel
import com.google.gson.JsonObject
import javax.inject.Inject

class AlertRepository   @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callAlert(objectLogin: JsonObject): AlertModel {
        return apiRequest {
            webServiceInterface.callAlert(objectLogin)
        }
    }
}