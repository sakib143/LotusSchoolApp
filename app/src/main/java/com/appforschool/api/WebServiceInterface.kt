package com.appforschool.api

import com.appforschool.data.model.*
import com.appforschool.utils.Constant
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

    @POST("Get_SpData")
    suspend fun callSetJoinLog(@Body jsonObject: JsonObject): Response<SetJoinModel>

    @POST("Get_SpData")
    suspend fun callFileViewLog(@Body jsonObject: JsonObject): Response<FileViewLogModel>

    @POST("Get_SpData")
    suspend fun callEndLog(@Body jsonObject: JsonObject): Response<SetCallEndLogModel>

    @POST("GetVersion")
    suspend fun callLatestVersion(@Body jsonObject: JsonObject): Response<GetVersionModel>

    @POST("Get_SpData")
    suspend fun callHomeData(@Body jsonObject: JsonObject): Response<HomeApiModel>

    @POST("Get_SpData")
    suspend fun callUpdateProfile(@Body jsonObject: JsonObject): Response<UpdateProfileModel>

    @POST("Get_SpData")
    suspend fun callDeleteDrive(@Body jsonObject: JsonObject): Response<DeleteDriveModel>


    @Multipart
    @POST("UploadFile")
    suspend fun callUploadAssignment(
        @Part Image: MultipartBody.Part,
        @Part(Constant.REUQEST_SHARE_ID) shareid: RequestBody,
        @Part(Constant.REUQEST_USER_ID) userid: RequestBody,
        @Part(Constant.REQUEST_USER_TYPE) usertype: RequestBody,
        @Part(Constant.REQUEST_STUDENTID) studentid: RequestBody,
        @Part(Constant.REQUEST_FILE_TITLE) filetitle: RequestBody,
        @Part(Constant.REQUEST_FILE_DESCR) filedescr: RequestBody,
        @Part(Constant.REQUEST_FILE_TYPE) filetype: RequestBody,
        @Part(Constant.REQUEST_FILE_EXT) fileext: RequestBody,
        @Part(Constant.REQUEST_FILE_SIZE) filesize: RequestBody,
        @Part(Constant.REUQEST_UPLOAD_TYPE) uploadtype: RequestBody
    ): Response<AssignmentSubmissionModel>

    @Multipart
    @POST("UploadStudentSubjectiveAnswerFile")
    suspend fun uploadAnswerFile(
        @Part Image: MultipartBody.Part,
        @Part(Constant.REQUEST_EXAM_ID) strExamId: RequestBody,
        @Part(Constant.REQUEST_QUESTION_ID) strQuestionId: RequestBody,
        @Part(Constant.REQUEST_STUDENTID) strStudentId: RequestBody,
        @Part(Constant.REUQEST_USER_ID) strUserId: RequestBody,
        ): Response<UploadAnswerFileModel>

    @POST("Get_SpData")
    suspend fun callStandardListForAddDrive(@Body jsonObject: JsonObject): Response<StandardListModel>

    @POST("Get_SpData")
    suspend fun callSubjectListForAddDrive(@Body jsonObject: JsonObject): Response<SubjectListModel>

    @POST("Get_SpData")
    suspend fun callLinkAddDrive(@Body jsonObject: JsonObject): Response<UploadFileUrlModel>

    @Multipart
    @POST("UploadFile")
    suspend fun callFileAddDrive(
        @Part Image: MultipartBody.Part,
        @Part(Constant.REUQEST_SHARE_ID) shareid: RequestBody,
        @Part(Constant.REUQEST_USER_ID) userid: RequestBody,
        @Part(Constant.REQUEST_USER_TYPE) usertype: RequestBody,
        @Part(Constant.REQUEST_STUDENTID) studentid: RequestBody,
        @Part(Constant.REQUEST_FILE_TITLE) filetitle: RequestBody,
        @Part(Constant.REQUEST_FILE_DESCR) filedescr: RequestBody,
        @Part(Constant.REQUEST_FILE_TYPE) filetype: RequestBody,
        @Part(Constant.REQUEST_FILE_EXT) fileext: RequestBody,
        @Part(Constant.REQUEST_FILE_SIZE) filesize: RequestBody,
        @Part(Constant.REUQEST_UPLOAD_TYPE) uploadtype: RequestBody,
        @Part(Constant.REQUEST_KW_TYPE) kwType: RequestBody,
        @Part(Constant.REUQEST_SUBJECT_ID) subjectId: RequestBody,
        @Part(Constant.REQUEST_STANDARDID) standardId: RequestBody
    ): Response<AssignmentSubmissionModel>

    @Multipart
    @POST("UploadFile")
    suspend fun callWithoutFileAddDrive(
        @Part(Constant.REUQEST_SHARE_ID) shareid: RequestBody,
        @Part(Constant.REUQEST_USER_ID) userid: RequestBody,
        @Part(Constant.REQUEST_USER_TYPE) usertype: RequestBody,
        @Part(Constant.REQUEST_STUDENTID) studentid: RequestBody,
        @Part(Constant.REQUEST_FILE_TITLE) filetitle: RequestBody,
        @Part(Constant.REQUEST_FILE_DESCR) filedescr: RequestBody,
        @Part(Constant.REQUEST_FILE_TYPE) filetype: RequestBody,
        @Part(Constant.REUQEST_UPLOAD_TYPE) uploadtype: RequestBody,
        @Part(Constant.REQUEST_KW_TYPE) kwType: RequestBody,
        @Part(Constant.REUQEST_SUBJECT_ID) subjectId: RequestBody,
        @Part(Constant.REQUEST_STANDARDID) standardId: RequestBody
    ): Response<AssignmentSubmissionModel>

    @POST("Get_SpData")
    suspend fun callChangePasswordd(@Body jsonObject: JsonObject): Response<ChangePasswordModel>

    @Multipart
    @POST("UpdateProfilePic")
    suspend fun callChnageProfilePic(
        @Part Image: MultipartBody.Part,
        @Part(Constant.REUQEST_USER_ID) userid : RequestBody) : Response<ChangeProfilePicModel>

    @POST("Get_SpData")
    suspend fun callAttendExam(@Body jsonObject: JsonObject): Response<AttendExamModel>

    @POST("Get_SpData")
    suspend fun callUpdateExamAnswer(@Body jsonObject: JsonObject): Response<UpdateExamAnswerModel>

    @POST("Get_SpData")
    suspend fun callEndExam(@Body jsonObject: JsonObject): Response<EndExamModel>

    @POST("Get_SpData")
    suspend fun callStartExam(@Body jsonObject: JsonObject): Response<StartExamModel>

    @POST("Get_SpData")
    suspend fun callViewResult(@Body jsonObject: JsonObject): Response<ViewResultModel>

    @POST("Get_SpData")
    suspend fun callInActiveSchedule(@Body jsonObject: JsonObject): Response<InActiveModel>

}