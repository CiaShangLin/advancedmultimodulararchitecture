package com.shang.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.shang.domain.model.ErrorMessage
import com.shang.presentation.view.renderEmptyScreen
import com.shang.presentation.view.renderErrorFullScreen
import com.shang.presentation.view.renderErrorPopup
import com.shang.presentation.view.renderLoadingFullScreen
import com.shang.presentation.view.renderLoadingPopup

sealed class StateRenderer<out S, O> {

    class ScreenContent<S, O>(val viewState: S) : StateRenderer<S, O>()

    data class LoadingPopup<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class LoadingFullScreen<S, O>(val viewState: S, @StringRes val loadingMessage: Int = R.string.loading) : StateRenderer<S, O>()

    data class ErrorPopup<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class ErrorFullScreen<S, O>(val viewState: S, val errorMessage: ErrorMessage) : StateRenderer<S, O>()

    data class Empty<S, O>(val viewState: S, @StringRes val emptyMessage: Int = R.string.no_data) : StateRenderer<S, O>()

    data class Success<S, O>(val output: O) : StateRenderer<S, O>()

    @Composable
    fun onUiState(action: @Composable (S) -> Unit): StateRenderer<S, O> {
        if (this is ScreenContent) {
            action(viewState)
        }
        return this
    }

    @Composable
    fun onLoadingState(action: @Composable (S) -> Unit): StateRenderer<S, O> {
        if (this is LoadingPopup) {
            action(viewState)
        } else if (this is LoadingFullScreen) {
            action(viewState)
        }
        return this
    }

    fun onSuccessState(action: (O) -> Unit): StateRenderer<S, O> {
        if (this is Success) {
            action(output)
        }
        return this
    }

    @Composable
    fun onErrorState(action: @Composable (S) -> Unit): StateRenderer<S, O> {
        if (this is ErrorPopup) {
            action(viewState)
        } else if (this is ErrorFullScreen) {
            action(viewState)
        }
        return this
    }

    fun onEmptyState(action: () -> Unit): StateRenderer<S, O> {
        if (this is Empty) {
            action()
        }
        return this
    }

    companion object {
        @Composable
        fun <S, O> of(
            retryAction: () -> Unit = {},
            stateRenderer: StateRenderer<S, O>,
            block: @Composable StateRenderer<S, O>.() -> Unit,
        ): StateRenderer<S, O> {
            stateRenderer.block() // show this first before doing any thing

            when (stateRenderer) {
                is Empty -> renderEmptyScreen(stateRenderer.emptyMessage)
                is ErrorFullScreen -> renderErrorFullScreen(stateRenderer.errorMessage, retryAction)
                is ErrorPopup -> renderErrorPopup(stateRenderer.errorMessage, retryAction)
                is LoadingFullScreen -> renderLoadingFullScreen(stateRenderer.loadingMessage)
                is LoadingPopup -> renderLoadingPopup(stateRenderer.loadingMessage)
                else -> {}
            }
            return stateRenderer
        }
    }
}
