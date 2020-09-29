package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.SubjectModel
import com.google.gson.JsonObject
import javax.inject.Inject

class SubjectRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callSubject(inputParam: JsonObject): SubjectModel {
        return apiRequest {
            webServiceInterface.callSubject(inputParam)
        }
    }
}