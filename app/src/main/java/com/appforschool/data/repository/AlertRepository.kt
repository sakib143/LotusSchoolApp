package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.LoginModel
import com.google.gson.JsonObject
import javax.inject.Inject

class AlertRepository   @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callLogin(objectLogin: JsonObject): LoginModel {
        return apiRequest {
            webServiceInterface.calllLogin(objectLogin)
        }
    }
}