package com.shang.domain.usecase

import com.shang.domain.model.ErrorMessage
import com.shang.domain.result.OutCome

interface UseCase<R> {
    suspend fun onSuccess(success: OutCome.Success<R>)
    suspend fun onEmpty()
    suspend fun onError(errorMessage: ErrorMessage)
}
