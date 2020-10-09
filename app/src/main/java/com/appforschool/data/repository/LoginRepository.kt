package com.appforschool.data.repository

import com.google.gson.JsonObject
import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.GetVersionModel
import com.appforschool.data.model.LoginModel
import javax.inject.Inject

class LoginRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callLogin(objectLogin: JsonObject): LoginModel {
        return apiRequest {
            webServiceInterface.calllLogin(objectLogin)
        }
    }

    suspend fun callLatestVersion(objectLogin: JsonObject): GetVersionModel {
        return apiRequest {
            webServiceInterface.callLatestVersion(objectLogin)
        }
    }
}