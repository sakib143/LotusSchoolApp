package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.*
import com.google.gson.JsonObject
import javax.inject.Inject

class AddToDriveRepository   @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    suspend fun callStandardListForAddDrive(objectLogin: JsonObject): StandardListModel {
        return apiRequest {
            webServiceInterface.callStandardListForAddDrive(objectLogin)
        }
    }

    suspend fun callSubjectListForAddDrive(objectLogin: JsonObject): SubjectListModel {
        return apiRequest {
            webServiceInterface.callSubjectListForAddDrive(objectLogin)
        }
    }

    suspend fun callUploadFileUrlModelDrive(objectLogin: JsonObject): UploadFileUrlModel {
        return apiRequest {
            webServiceInterface.callUploadFileUrlModelDrive(objectLogin)
        }
    }

    fun knowledgeTypeList() : ArrayList<KnwledgeTypeModel>{
        val arrayList : ArrayList<KnwledgeTypeModel> = ArrayList<KnwledgeTypeModel>()
        arrayList.add(KnwledgeTypeModel("A","Assignment"))
        arrayList.add(KnwledgeTypeModel("Q","Question Paper"))
        arrayList.add(KnwledgeTypeModel("B","Textbook"))
        arrayList.add(KnwledgeTypeModel("N","Notes"))
        arrayList.add(KnwledgeTypeModel("S","Solved Paper"))
        arrayList.add(KnwledgeTypeModel("D","Digest"))
        arrayList.add(KnwledgeTypeModel("R","Research"))
        arrayList.add(KnwledgeTypeModel("F","Ref. Book"))
        arrayList.add(KnwledgeTypeModel("T","Test/Quiz"))
        return arrayList
    }

}