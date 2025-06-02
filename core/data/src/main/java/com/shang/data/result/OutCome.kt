package com.shang.data.result

import com.shang.data.model.ErrorMessage

sealed class OutCome<T> {
  abstract fun isSuccess(): Boolean

  open val errorMessage: ErrorMessage? = null

  abstract suspend fun accept(useCase: UseCase<T>)

  class Success<T>(val data: T) : OutCome<T>() {
    override fun isSuccess(): Boolean = true
    override suspend fun accept(useCase: UseCase<T>) {
      useCase.onSuccess(this)
    }
  }
}
