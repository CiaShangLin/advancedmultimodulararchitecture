package com.shang.data.error

import com.google.gson.Gson
import com.shang.data.model.ErrorMessage
import com.shang.data.response.ErrorResponse

fun ErrorResponse.toDomain(code: Int): ErrorMessage {
    return ErrorMessage(
        code = code,
        message = errorMessage.orEmpty(),
        errorFieldList = errorFieldList.orEmpty(),
    )
}

fun getDefaultErrorResponse() = ErrorResponse("", "", emptyList())

fun getErrorResponse(gson: Gson, errorBodyString: String): ErrorResponse {
    return try {
        gson.fromJson(errorBodyString, ErrorResponse::class.java)
    } catch (e: Exception) {
        getDefaultErrorResponse()
    }
}
