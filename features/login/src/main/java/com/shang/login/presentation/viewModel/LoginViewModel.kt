package com.shang.login.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shang.login.domain.usecase.LoginUseCase
import com.shang.login.presentation.error.LoginError
import com.shang.login.presentation.protocol.LoginInput
import com.shang.login.presentation.protocol.LoginOutput
import com.shang.login.presentation.protocol.LoginViewState
import com.shang.login.presentation.validator.LoginValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private var _loginViewState = LoginViewState()

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
        _loginViewState = _loginViewState.updateState()
        validate()
    }

    private fun validate() {
        val userNameError: LoginError = LoginValidator.userNameError(_loginViewState.userName)
        val passwordError: LoginError = LoginValidator.passwordError(_loginViewState.password)
        val isLoginButtonEnabled: Boolean = LoginValidator.canDoLogin(userNameError, passwordError)

        _loginViewState = _loginViewState.copy(
            isLoginButtonEnabled = isLoginButtonEnabled,
            userNameError = userNameError,
            passwordError = passwordError,
        )
    }

    private fun sendOutput(action: () -> LoginOutput) {
        viewModelScope.launch {
            _viewOutput.send(action())
        }
    }

    fun login() {
        viewModelScope.launch {
            loginUseCase.execute(
                input = LoginUseCase.Input(
                    username = _loginViewState.userName,
                    password = _loginViewState.password
                ),
                onSuccess = {

                },
                onError = {

                }
            )
        }
    }
}
