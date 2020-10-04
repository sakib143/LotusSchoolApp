package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.LoginModel
import com.google.gson.JsonObject
import javax.inject.Inject

class AssignmentRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callAssignment(inputParam: JsonObject): AssignmentModel {
        return apiRequest {
            webServiceInterface.callAssignment(inputParam)
        }
    }
}