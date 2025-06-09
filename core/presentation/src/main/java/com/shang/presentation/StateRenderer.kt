package com.shang.presentation

import androidx.annotation.StringRes
import com.shang.domain.model.ErrorMessage

sealed class StateRenderer<out S, O> {

    class ScreenContent<S, O>(val viewState: S) : StateRenderer<S, O>()

    data class LoadingPopup<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class FullScreenLoading<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class ErrorPopup<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class FullScreenError<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class Empty<S, O>(val viewState: S, @StringRes val emptyMessage: Int = R.string.no_data) : StateRenderer<S, O>()

    data class Success<S, O>(val output: O) : StateRenderer<S, O>()
}
