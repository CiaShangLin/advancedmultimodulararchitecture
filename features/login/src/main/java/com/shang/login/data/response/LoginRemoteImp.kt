package com.shang.login.data.response

import com.shang.data.mapper.toDomain
import com.shang.data.result.OutCome
import com.shang.data.source.NetworkDataSource
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.service.LoginService
import com.shang.login.data.source.LoginRemote
import com.shang.login.domain.mapper.LoginMapperInterface
import com.shang.login.domain.model.User

class LoginRemoteImp(
    private val networkDataService: NetworkDataSource<LoginService>,
    private val loginMapper: LoginMapperInterface,
) : LoginRemote {
    override suspend fun login(loginRequestBody: LoginRequestBody): OutCome<User> {
        return networkDataService.performRequest(
            request = { login(loginRequestBody).await() },
            onSuccess = { response, _ -> OutCome.success(loginMapper.toDomain(response)) },
            onError = { errorResponse, code -> OutCome.error(errorResponse.toDomain(code)) },
        )
    }
}
