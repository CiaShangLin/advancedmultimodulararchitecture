package com.shang.data.mapper

import com.shang.data.response.ErrorResponse
import com.shang.domain.model.ErrorMessage

fun ErrorResponse.toDomain(code: Int): ErrorMessage {
    return ErrorMessage(
        code = code,
        message = errorMessage.orEmpty(),
        errorFieldList = errorFieldList ?: emptyList(),
    )
}
