package com.appforschool.utils

import android.content.Context
import android.content.SharedPreferences
import com.appforschool.data.model.LoginModel
import com.google.gson.Gson
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
    private val SAVE_USER_DATA = "saveUserData"
    private val IS_UPDATE_APP_VISIBLE = "is_update_app_visible"


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

    fun saveUserId(usedrId: String,userDataModel  : LoginModel.Data) {
        val editor: SharedPreferences.Editor = mPref.edit()
        //Save user id
        editor.putString(USER_ID, usedrId)
        //Save user model data
        val gson = Gson();
        val jsonToString = gson.toJson(userDataModel)
        editor.putString(SAVE_USER_DATA, jsonToString)
        editor.apply()
    }

    fun getUserData () : LoginModel.Data? {
        val gson = Gson();
        val data = mPref.getString(SAVE_USER_DATA,null)
        val model : LoginModel.Data  = gson.fromJson(data, LoginModel.Data::class.java)
        return model
    }

    fun setUpdateAppDialog(isDialogShow: Boolean) {
        val editor: SharedPreferences.Editor = mPref.edit()
        editor.putBoolean(IS_UPDATE_APP_VISIBLE, isDialogShow)
        editor.apply()
    }

    fun isUpdateDialogVisible() : Boolean{
        return mPref.getBoolean(IS_UPDATE_APP_VISIBLE, false)
    }


}