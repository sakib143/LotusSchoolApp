package com.appforschool.api

import com.appforschool.utils.Constant
import com.appforschool.utils.LogM
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException

abstract class SafeAPIRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.code() == Constant.SUCCESS_CODE) {
            return response.body()!!
        } else if (response.code() == Constant.UNAUTHORIZED_CODE) {
            //Todo: APi call response Check failer data with failer code
            val error = response.errorBody()?.string()
            val message = StringBuffer()
            error.let {
                try {
                    message.append("Error 401 UNAUTHORIZED CODE")
                } catch (e: JSONException) {

                }
                throw ApiExceptions(message.toString())
            }
        } else if (response.code() == Constant.INTERNAL_SERVER_ERROR_CODE) {
            //Todo: APi call response Check failer data with failer code
            val error = response.errorBody()?.string()
            // return response.errorBody()!!
            val message = StringBuffer()
            error.let {
                try {
                    message.append("Error 500 Internal Server Error")
                } catch (e: JSONException) {

                }
                throw ApiExceptions(message.toString())
            }
        } else if (response.code() == Constant.NO_DATA_FOUND_CODE) {
            //Todo: APi call response Check failer data with failer code
            val error = response.errorBody()?.string()
            val message = StringBuffer()
            error.let {
                try {
                    message.append("Error 404 DataNot Found")
                } catch (e: JSONException) {

                }
                throw ApiExceptions(message.toString())
            }
        } else if (response.code() == Constant.NON_AUTHORITATIVE_INFORMATION) {
            //Todo: APi call response Check failer data with failer code
            val error = response.errorBody()?.string()
            val message = StringBuffer()
            error.let {
                try {
                    message.append("203 non-authoritative information")
                } catch (e: JSONException) {

                }
                throw ApiExceptions(message.toString())
            }
        } else if (response.code() == Constant.METHOD_NOT_ALLOW) {
            //Todo: APi call response Check failer data with failer code
            val error = response.errorBody()?.string()
            val message = StringBuffer()
            error.let {
                try {
                    message.append("405 Method Not Allowed")
                } catch (e: JSONException) {

                }
                throw ApiExceptions(message.toString())
            }
        } else {
            //Todo: APi call response Check failer data with failer code
            val error = response.errorBody()?.string()
            val message = StringBuffer()
            error.let {
                try {
                    message.append(JSONObject(it).getString(Constant.RESPONSE_MESSAGE))
                } catch (e: JSONException) {

                }
                throw ApiExceptions(message.toString())
            }
        }
    }

}