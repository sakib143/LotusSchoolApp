package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.DriveModel
import com.google.gson.JsonObject
import javax.inject.Inject

class DriveRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callDriveList(objectLogin: JsonObject): DriveModel {
        return apiRequest {
            webServiceInterface.callDriveList(objectLogin)
        }
    }
}