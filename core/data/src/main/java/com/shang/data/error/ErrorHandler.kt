package com.shang.data.error

import com.google.gson.Gson
import com.shang.data.response.ErrorResponse

fun getDefaultErrorResponse() = ErrorResponse("", "", emptyList())

fun getErrorResponse(gson: Gson, errorBodyString: String): ErrorResponse {
    return try {
        gson.fromJson(errorBodyString, ErrorResponse::class.java)
    } catch (e: Exception) {
        getDefaultErrorResponse()
    }
}
