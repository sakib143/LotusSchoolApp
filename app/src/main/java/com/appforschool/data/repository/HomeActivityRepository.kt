package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.SetJoinModel
import com.google.gson.JsonObject
import javax.inject.Inject

class HomeActivityRepository @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callSetJoinLog(objectLogin: JsonObject): SetJoinModel {
        return apiRequest {
            webServiceInterface.callSetJoinLog(objectLogin)
        }
    }
}