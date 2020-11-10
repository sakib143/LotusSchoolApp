package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.SetCallEndLogModel
import com.appforschool.data.model.UpdateProfileModel
import com.google.gson.JsonObject
import javax.inject.Inject

class UpdateProfileRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callUpdateProfile(objectLogin: JsonObject): UpdateProfileModel {
        return apiRequest {
            webServiceInterface.callUpdateProfile(objectLogin)
        }
    }
}