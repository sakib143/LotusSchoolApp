package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.SetCallEndLogModel
import com.google.gson.JsonObject
import javax.inject.Inject

class VideoCallingRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callEndLog(objectLogin: JsonObject): SetCallEndLogModel {
        return apiRequest {
            webServiceInterface.callEndLog(objectLogin)
        }
    }
}