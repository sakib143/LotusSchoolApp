package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AssignmentSubmissionModel
import com.appforschool.data.model.ChangeProfilePicModel
import com.appforschool.data.model.SetCallEndLogModel
import com.appforschool.data.model.UpdateProfileModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateProfileRepository  @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callUpdateProfile(objectLogin: JsonObject): UpdateProfileModel {
        return apiRequest {
            webServiceInterface.callUpdateProfile(objectLogin)
        }
    }

    suspend fun callChnageProfilePic(
        Image : MultipartBody.Part,
        userId: RequestBody
    ): ChangeProfilePicModel {
        return apiRequest {
            webServiceInterface.callChnageProfilePic(Image, userId)
        }
    }

}