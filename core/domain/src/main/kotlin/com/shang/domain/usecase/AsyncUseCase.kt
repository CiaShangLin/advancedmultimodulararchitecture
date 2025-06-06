package com.shang.domain.usecase

import com.shang.domain.model.ErrorMessage
import com.shang.domain.result.OutCome

abstract class AsyncUseCase<I, R> : UseCase<R> {

    private lateinit var success: suspend (R) -> Unit
    private lateinit var error: suspend (ErrorMessage) -> Unit
    private lateinit var empty: suspend () -> Unit

    suspend fun execute(
        input: I,
        onSuccess: suspend (R) -> Unit = {},
        onError: suspend (ErrorMessage) -> Unit = {},
        onEmpty: suspend () -> Unit = {},
    ) {
        this.success = onSuccess
        this.error = onError
        this.empty = onEmpty
        run(input).accept(this)
    }

    abstract fun run(input: I): OutCome<R>

    override suspend fun onSuccess(success: OutCome.Success<R>) {
        success(success.data)
    }

    override suspend fun onError(errorMessage: ErrorMessage) {
        error(errorMessage)
    }

    override suspend fun onEmpty() {
        empty()
    }
}
