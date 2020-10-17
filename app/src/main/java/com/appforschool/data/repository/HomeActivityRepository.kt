package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AssignmentSubmissionModel
import com.appforschool.data.model.FileViewLogModel
import com.appforschool.data.model.SetJoinModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class HomeActivityRepository @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callSetJoinLog(objectLogin: JsonObject): SetJoinModel {
        return apiRequest {
            webServiceInterface.callSetJoinLog(objectLogin)
        }
    }

    suspend fun callFileViewLogLog(inputParam: JsonObject): FileViewLogModel {
        return apiRequest {
            webServiceInterface.callFileViewLog(inputParam)
        }
    }

    suspend fun callUploadAssignment(
        Image : MultipartBody.Part,
        shareid: RequestBody,
        userid: RequestBody,
        usertype: RequestBody,
        studentid: RequestBody,
        filetitle: RequestBody,
        filedescr: RequestBody,
        filetype: RequestBody,
        fileext: RequestBody,
        filesize: RequestBody,
        uploadtype: RequestBody): AssignmentSubmissionModel {
        return apiRequest {
            webServiceInterface.callUploadAssignment(Image, shareid,userid,usertype,studentid,filetitle,filedescr,filetype,fileext,filesize,uploadtype)
        }
    }
}