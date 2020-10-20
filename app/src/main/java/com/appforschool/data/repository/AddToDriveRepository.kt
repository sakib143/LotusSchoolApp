package com.appforschool.data.repository

import com.appforschool.api.SafeAPIRequest
import com.appforschool.api.WebServiceInterface
import com.appforschool.data.model.AlertModel
import com.google.gson.JsonObject
import javax.inject.Inject

class AddToDriveRepository   @Inject constructor(
    private val webServiceInterface: WebServiceInterface
) : SafeAPIRequest() {

    fun knowledgeTypeList() : ArrayList<String>{
        val arrayList : ArrayList<String> = ArrayList<String>()
        arrayList.add("Select knowledge Type")
        arrayList.add("Assignment")
        arrayList.add("Notes")
        arrayList.add("Research")
        arrayList.add("Digest/Guide")
        arrayList.add("Test/Exam/Quiz")
        arrayList.add("Question Paper")
        arrayList.add("Textbook")
        arrayList.add("Ref.Book")
        arrayList.add("Solved Paper")
        arrayList.add("Other")
        return arrayList
    }

}