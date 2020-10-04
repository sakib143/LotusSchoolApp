package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.SubjectDetailsModel
import com.appforschool.data.model.SubjectModel
import com.google.gson.JsonObject
import javax.inject.Inject

class SubjectDetailsRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callSubjectDetails(inputParam: JsonObject): SubjectDetailsModel {
        return apiRequest {
            webServiceInterface.callSubjectDetails(inputParam)
        }
    }
}