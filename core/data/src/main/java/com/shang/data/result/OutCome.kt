package com.shang.data.result

import com.shang.domain.model.ErrorMessage

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

    class Error<T>(private val _errorMessage: ErrorMessage) : OutCome<T>() {

        override val errorMessage: ErrorMessage = _errorMessage

        override fun isSuccess(): Boolean = false

        override suspend fun accept(useCase: UseCase<T>) {
            useCase.onError(_errorMessage)
        }
    }

    class Empty<T> : OutCome<T>() {
        override fun isSuccess(): Boolean = true
        override suspend fun accept(useCase: UseCase<T>) {
            useCase.onEmpty()
        }
    }

    companion object {
        fun <T> success(data: T) = Success<T>(data)
        fun <T> error(errorMessage: ErrorMessage) = Error<T>(errorMessage)
        fun <T> empty() = Empty<T>()
    }
}
