package com.appforschool.data.repository

import com.google.gson.JsonObject
import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.ScheduleModel
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callScheduleData(objectLogin: JsonObject): ScheduleModel {
        return apiRequest {
            webServiceInterface.callSchedule(objectLogin)
        }
    }
}