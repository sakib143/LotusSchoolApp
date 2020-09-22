package com.appforschool.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Handles Shared Preferences through out the App
 */
@Suppress("unused")
class PrefUtils @Inject constructor(context: Context) {
    /**
     * Object of [android.content.SharedPreferences]
     * */
    private val mPref = context.getSharedPreferences(Constant.APP_PREFERENCES, Context.MODE_PRIVATE)

    private val USER_ID = "user_id"
    private val STUDENT_NAME = "studentname"


    /**
     * Method to clear All Stored Preferences
     */
    fun clearAll() {
        val mEditor = mPref.edit()
        mEditor.clear()
        mEditor.apply()
    }


    fun getUserId(): String? {
        return mPref.getString(USER_ID,"")
    }

    fun saveUserId(usedrId: String, studentname:String) {
        val editor: SharedPreferences.Editor = mPref.edit()
        editor.putString(USER_ID, usedrId)
        editor.putString(STUDENT_NAME, studentname)
        editor.apply()
    }

    fun getUserName(): String? {
        return mPref.getString(STUDENT_NAME,"")
    }

//    fun saveUserData(userDataModel  : GetUserDataModel.Data) {
//        val editor: SharedPreferences.Editor = mPref.edit()
//        val gson = Gson();
//        val jsonToString = gson.toJson(userDataModel)
//        editor.putString(SAVE_USER_DATA, jsonToString)
//        editor.commit()
//    }
//
//    fun getUserData () : GetUserDataModel.Data? {
//        val gson = Gson();
//        val data = mPref.getString(SAVE_USER_DATA,null)
//        val model : GetUserDataModel.Data  = gson.fromJson(data, GetUserDataModel.Data::class.java)
//        return model
//    }
//
//    fun saveUserId(custId : String){
//        val editor: SharedPreferences.Editor = mPref.edit()
//        editor.putString(USER_ID, custId)
//        editor.commit()
//    }
//
//    fun getUserId() : String?{
//        return mPref.getString(USER_ID, null)
//    }
//


}