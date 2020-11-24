package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.UpdateExamAnswerModel
import com.google.gson.JsonObject
import javax.inject.Inject

class AttendExamRepository   @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {
    suspend fun callAttendExam(objectLogin: JsonObject): AttendExamModel {
        return apiRequest {
            webServiceInterface.callAttendExam(objectLogin)
        }
    }

    suspend fun callUpdateExamAnswer(objectLogin: JsonObject): UpdateExamAnswerModel {
        return apiRequest {
            webServiceInterface.callUpdateExamAnswer(objectLogin)
        }
    }
}