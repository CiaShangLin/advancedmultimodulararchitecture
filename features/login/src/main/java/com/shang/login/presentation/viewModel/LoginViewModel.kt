package com.shang.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shang.login.domain.model.User
import com.shang.login.domain.usecase.LoginUseCase
import com.shang.login.presentation.error.LoginError
import com.shang.login.presentation.protocol.LoginInput
import com.shang.login.presentation.protocol.LoginOutput
import com.shang.login.presentation.protocol.LoginViewState
import com.shang.login.presentation.validator.LoginValidator
import com.shang.presentation.StateRenderer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private var loginViewState = LoginViewState()

    private val _stateRenderStateFlow = MutableStateFlow<StateRenderer<LoginViewState, User>>(StateRenderer.ScreenContent(loginViewState))
    val stateRenderStateFlow: StateFlow<StateRenderer<LoginViewState, User>> = _stateRenderStateFlow

    private val _viewOutput: Channel<LoginOutput> = Channel()
    val viewOutput = _viewOutput.receiveAsFlow()

    fun loginInput(loginInput: LoginInput) {
        when (loginInput) {
            LoginInput.LoginButtonClicked -> login()
            is LoginInput.PasswordUpdated -> updateState {
                copy(password = loginInput.password)
            }

            LoginInput.RegisterButtonClicked -> sendOutput { LoginOutput.NavigateToRegister }
            is LoginInput.UserNameUpdated -> updateState { copy(userName = loginInput.username) }
        }
    }

    private fun updateState(updateState: LoginViewState.() -> LoginViewState) {
        loginViewState = loginViewState.updateState()
        validate()
    }

    private fun validate() {
        val userNameError: LoginError = LoginValidator.userNameError(loginViewState.userName)
        val passwordError: LoginError = LoginValidator.passwordError(loginViewState.password)
        val isLoginButtonEnabled: Boolean = LoginValidator.canDoLogin(userNameError, passwordError)

        loginViewState = loginViewState.copy(
            isLoginButtonEnabled = isLoginButtonEnabled,
            userNameError = userNameError,
            passwordError = passwordError,
        )

        val stateRenderer = StateRenderer.ScreenContent<LoginViewState, User>(loginViewState)
        _stateRenderStateFlow.value = stateRenderer
    }

    private fun sendOutput(action: () -> LoginOutput) {
        viewModelScope.launch {
            _viewOutput.send(action())
        }
    }

    fun login() {
        viewModelScope.launch {
            val newStateRenderer = StateRenderer.LoadingPopup<LoginViewState, User>(loginViewState)
            _stateRenderStateFlow.value = newStateRenderer
            loginUseCase.execute(
                input = LoginUseCase.Input(
                    username = loginViewState.userName,
                    password = loginViewState.password,
                ),
                onSuccess = {
                    val newStateRenderer = StateRenderer.Success<LoginViewState, User>(it)
                    _stateRenderStateFlow.value = newStateRenderer
                },
                onError = {
                    val newStateRenderer = StateRenderer.ErrorPopup<LoginViewState, User>(loginViewState, it)
                    _stateRenderStateFlow.value = newStateRenderer
                },
            )
        }
    }
}
