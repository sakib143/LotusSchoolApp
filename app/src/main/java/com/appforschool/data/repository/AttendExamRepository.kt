package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun callEndExam(objectLogin: JsonObject): EndExamModel {
        return apiRequest {
            webServiceInterface.callEndExam(objectLogin)
        }
    }

    suspend fun uploadAnswerFile(
        Image : MultipartBody.Part,
        shareid: RequestBody,
        userid: RequestBody,
        usertype: RequestBody
    ): UploadAnswerFileModel {
        return apiRequest {
            webServiceInterface.uploadAnswerFile(Image, shareid,userid,usertype)
        }
    }

}