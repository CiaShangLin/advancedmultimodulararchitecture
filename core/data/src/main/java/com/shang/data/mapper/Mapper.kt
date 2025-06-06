package com.shang.data.mapper

import com.shang.data.model.ErrorMessage
import com.shang.data.response.ErrorResponse

fun ErrorResponse.toDomain(code: Int): ErrorMessage {
    return ErrorMessage(
        code = code,
        message = errorMessage.orEmpty(),
        errorFieldList = errorFieldList ?: emptyList(),
    )
}
