package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.HomeApiModel
import com.google.gson.JsonObject
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {


    suspend fun callHomeData(objectLogin: JsonObject): HomeApiModel {
        return apiRequest {
            webServiceInterface.callHomeData(objectLogin)
        }
    }
}