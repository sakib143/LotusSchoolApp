package com.learnathome.data.repository

import com.google.gson.JsonObject
import com.learnathome.api.SafeAPIRequest
import com.learnathome.api.WebServiceInterface
import com.learnathome.data.model.LoginModel
import com.learnathome.data.model.ScheduleModel
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