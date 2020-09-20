package com.learnathome.api

import com.google.gson.JsonObject
import com.learnathome.data.model.LoginModel
import com.learnathome.data.model.ScheduleModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * REST API access points
 */
interface WebServiceInterface {

    @POST("Login")
    suspend fun calllLogin(@Body jsonObject: JsonObject): Response<LoginModel>

    @POST("GetSchedule")
    suspend fun callSchedule(@Body jsonObject: JsonObject): Response<ScheduleModel>

//    @Multipart
//    @POST("UpdateProfilePic")
//    suspend fun callUpdateProfilePics(
//        @Part userPhotoBody : MultipartBody.Part,
//        @Part(Constant.REQUEST_USERID) driverNameBody : RequestBody
//    ) : Response<UpdateProfilePicsModel>


}