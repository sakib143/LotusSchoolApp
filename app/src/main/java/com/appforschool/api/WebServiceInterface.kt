package com.appforschool.api

import com.appforschool.data.model.*
import com.google.gson.JsonObject
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

    @POST("GetSubjects")
    suspend fun callSubject(@Body jsonObject: JsonObject): Response<SubjectModel>

    @POST("Get_SpData")
    suspend fun callSubjectDetails(@Body jsonObject: JsonObject): Response<SubjectDetailsModel>

    @POST("Get_SpData")
    suspend fun callAssignment(@Body jsonObject: JsonObject): Response<AssignmentModel>



//    @Multipart
//    @POST("UpdateProfilePic")
//    suspend fun callUpdateProfilePics(
//        @Part userPhotoBody : MultipartBody.Part,
//        @Part(Constant.REQUEST_USERID) driverNameBody : RequestBody
//    ) : Response<UpdateProfilePicsModel>


}