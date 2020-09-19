package com.learnathome.utils

import android.content.Context
import android.content.SharedPreferences
import com.learnathome.utils.Constant
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

    private val SAVE_USER_DATA = "saveUserData"
    private val USER_ID = "user_id"
    private val IS_NEW_USER = "is_new_user"

    fun getIsNewUser() : Boolean {
        return mPref.getBoolean(IS_NEW_USER, true)
    }

    fun setIsNewUser(value: Boolean){
        val editor: SharedPreferences.Editor = mPref.edit()
        editor.putBoolean(IS_NEW_USER, value)
        editor.commit()
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
//    /**
//     * Method to clear All Stored Preferences
//     */
//    fun clearAll() {
//        val mEditor = mPref.edit()
//        mEditor.clear()
//        mEditor.apply()
//    }

}