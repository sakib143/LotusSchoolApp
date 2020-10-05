package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AlertModel
import com.appforschool.data.model.ExamModel
import com.google.gson.JsonObject
import javax.inject.Inject

class ExamRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callExam(objectLogin: JsonObject): ExamModel {
        return apiRequest {
            webServiceInterface.callExam(objectLogin)
        }
    }
}