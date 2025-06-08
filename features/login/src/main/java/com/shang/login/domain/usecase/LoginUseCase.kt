package com.shang.login.domain.usecase

import com.shang.domain.result.OutCome
import com.shang.domain.usecase.AsyncUseCase
import com.shang.login.data.source.LoginRemote
import com.shang.login.domain.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRemote: LoginRemote) :
    AsyncUseCase<LoginUseCase.Input, User>() {

    data class Input(val username: String, val password: String)

    override suspend fun run(input: Input): OutCome<User> {
        return loginRemote.login(input.username, input.password)
    }
}
