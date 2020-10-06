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

    @POST("Get_SpData")
    suspend fun callSchedule(@Body jsonObject: JsonObject): Response<ScheduleModel>

    @POST("Get_SpData")
    suspend fun callSubject(@Body jsonObject: JsonObject): Response<SubjectModel>

    @POST("Get_SpData")
    suspend fun callSubjectDetails(@Body jsonObject: JsonObject): Response<SubjectDetailsModel>

    @POST("Get_SpData")
    suspend fun callAssignment(@Body jsonObject: JsonObject): Response<AssignmentModel>

    @POST("Get_SpData")
    suspend fun callAlert(@Body jsonObject: JsonObject): Response<AlertModel>

    @POST("Get_SpData")
    suspend fun callExam(@Body jsonObject: JsonObject): Response<ExamModel>

    @POST("Get_SpData")
    suspend fun callDriveList(@Body jsonObject: JsonObject): Response<DriveModel>



//    @Multipart
//    @POST("UpdateProfilePic")
//    suspend fun callUpdateProfilePics(
//        @Part userPhotoBody : MultipartBody.Part,
//        @Part(Constant.REQUEST_USERID) driverNameBody : RequestBody
//    ) : Response<UpdateProfilePicsModel>


}