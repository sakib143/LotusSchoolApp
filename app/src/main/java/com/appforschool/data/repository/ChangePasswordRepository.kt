package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.ChangePasswordModel
import com.google.gson.JsonObject
import javax.inject.Inject

class ChangePasswordRepository @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callChangePasswordd(inputParam: JsonObject): ChangePasswordModel {
        return apiRequest {
            webServiceInterface.callChangePasswordd(inputParam)
        }
    }
}