package com.learnathome.data.repository

import com.google.gson.JsonObject
import com.learnathome.api.SafeAPIRequest
import com.learnathome.api.WebServiceInterface
import com.learnathome.data.model.LoginModel
import javax.inject.Inject

class LoginRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callLogin(objectLogin: JsonObject): LoginModel {
        return apiRequest {
            webServiceInterface.calllLogin(objectLogin)
        }
    }
}