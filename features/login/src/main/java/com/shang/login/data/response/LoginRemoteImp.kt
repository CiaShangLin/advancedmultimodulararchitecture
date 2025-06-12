package com.shang.login.data.response

import com.shang.data.mapper.toDomain
import com.shang.data.source.NetworkDataSource
import com.shang.domain.model.User
import com.shang.domain.result.OutCome
import com.shang.login.data.request.LoginRequestBody
import com.shang.login.data.service.LoginService
import com.shang.login.data.source.LoginRemote
import com.shang.login.domain.mapper.LoginMapperInterface

class LoginRemoteImp(
    private val networkDataService: NetworkDataSource<LoginService>,
    private val loginMapper: LoginMapperInterface,
) : LoginRemote {
    override suspend fun login(username: String, password: String): OutCome<User> {
        return networkDataService.performRequest(
            request = {
                login(
                    LoginRequestBody(
                        username = username,
                        password = password,
                    ),
                ).await()
            },
            onSuccess = { response, _ -> OutCome.success(loginMapper.toDomain(response)) },
            onError = { errorResponse, code -> OutCome.error(errorResponse.toDomain(code)) },
        )
    }
}
