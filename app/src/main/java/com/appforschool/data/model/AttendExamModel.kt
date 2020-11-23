package com.appforschool.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class AttendExamModel(
    @Expose
    @SerializedName("data") var `data`: List<Data> = listOf(),
    @Expose
    @SerializedName("message") var message: String = "",
    @Expose
    @SerializedName("status") var status: Boolean = false
) {
    data class Data(
        @Expose
        @SerializedName("CreatedBy") var createdBy: Int = 0,
        @Expose
        @SerializedName("CreatedOn") var createdOn: String = "",
        @Expose
        @SerializedName("DispExamQuestionMarks") var dispExamQuestionMarks: String = "",
        @Expose
        @SerializedName("ExamID") var examID: Int = 0,
        @Expose
        @SerializedName("HdrFileName") var hdrFileName: Any = Any(),
        @Expose
        @SerializedName("IsACorrect") var isACorrect: Boolean = false,
        @Expose
        @SerializedName("IsActive") var isActive: Boolean = false,
        @Expose
        @SerializedName("IsBCorrect") var isBCorrect: Boolean = false,
        @Expose
        @SerializedName("IsCCorrect") var isCCorrect: Boolean = false,
        @Expose
        @SerializedName("IsDCorrect") var isDCorrect: Boolean = false,
        @Expose
        @SerializedName("IsReqContentNotFound") var isReqContentNotFound: Int = 0,
        @Expose
        @SerializedName("Marks") var marks: Double = 0.0,
        @Expose
        @SerializedName("OptionA") var optionA: String = "",
        @Expose
        @SerializedName("OptionB") var optionB: String = "",
        @Expose
        @SerializedName("OptionC") var optionC: String = "",
        @Expose
        @SerializedName("OptionD") var optionD: String = "",
        @Expose
        @SerializedName("QType") var qType: String = "",
        @Expose
        @SerializedName("QuestionDesc") var questionDesc: String = "",
        @Expose
        @SerializedName("QuestionFileName") var questionFileName: Any = Any(),
        @Expose
        @SerializedName("QuestionFileSize") var questionFileSize: Any = Any(),
        @Expose
        @SerializedName("QuestionOrder") var questionOrder: Int = 0,
        @Expose
        @SerializedName("ReqContentNotFoundColor") var reqContentNotFoundColor: String = "",
        @Expose
        @SerializedName("SrNo") var srNo: Int = 0,
        @Expose
        @SerializedName("SubjectiveAnswer") var subjectiveAnswer: Any = Any()
    )
}