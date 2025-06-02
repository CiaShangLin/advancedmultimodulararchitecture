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

  class Error(private val _errorMessage: ErrorMessage) : OutCome<Nothing>() {

    override val errorMessage: ErrorMessage = _errorMessage

    override fun isSuccess(): Boolean = false

    override suspend fun accept(useCase: UseCase<Nothing>) {
      useCase.onError(_errorMessage)
    }
  }

  class Empty : OutCome<Nothing>() {
    override fun isSuccess(): Boolean = true
    override suspend fun accept(useCase: UseCase<Nothing>) {
      useCase.onEmpty()
    }
  }

  companion object {
    fun <T> success(data: T) = Success<T>(data)
    fun error(errorMessage: ErrorMessage) = Error(errorMessage)
    fun empty() = Empty()
  }
}
